#!/usr/bin/env python3
"""Inspect native Codex discovery through its supported app-server protocol.

Usage: python3 scripts/codex_runtime.py --project PATH [--project PATH ...]
       [--trust-reviewed-hooks] [--dry-run]

Only the audit-log path is printed. Hook approval is an explicit initial trust
handshake for this synchronizer's reviewed dispatcher, never an allow rule.
"""
from __future__ import annotations

import argparse
import datetime
import hashlib
import json
import os
import pathlib
import selectors
import signal
import subprocess
import time
import traceback


class AppServer:
    """One bounded stdio transport; caller owns its lifetime and audit sink."""

    def __init__(self, cwd: pathlib.Path, errors):
        self.process = subprocess.Popen(["codex", "app-server"], cwd=cwd,
                                        stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                        stderr=errors, start_new_session=True)
        self.selector = selectors.DefaultSelector()
        self.selector.register(self.process.stdout, selectors.EVENT_READ)
        self.buffer = b""
        self.sequence = 0

    def send(self, value):
        self.process.stdin.write((json.dumps(value) + "\n").encode())
        self.process.stdin.flush()

    def receive(self, timeout: float):
        deadline = time.monotonic() + timeout
        while time.monotonic() < deadline:
            if b"\n" in self.buffer:
                line, self.buffer = self.buffer.split(b"\n", 1)
                return json.loads(line)
            if not self.selector.select(max(0, deadline - time.monotonic())):
                break
            chunk = os.read(self.process.stdout.fileno(), 65536)
            if not chunk:
                raise RuntimeError(f"app-server exited with {self.process.poll()}")
            self.buffer += chunk
        raise TimeoutError(f"app-server: no message within {timeout}s")

    def request(self, method: str, params: dict, timeout: float = 45, notifications=None):
        self.sequence += 1
        number = self.sequence
        self.send({"id": number, "method": method, "params": params})
        deadline = time.monotonic() + timeout
        while time.monotonic() < deadline:
            message = self.receive(max(0, deadline - time.monotonic()))
            if message.get("id") == number and "method" not in message:
                if "error" in message:
                    raise RuntimeError(f"{method}: {message['error']}")
                return message["result"]
            if "id" in message and "method" in message:
                self.send({"id": message["id"], "error": {"code": -32601, "message": "unattended request cannot approve"}})
            elif notifications is not None:
                notifications.append(message)
        raise TimeoutError(f"{method}: no response within {timeout}s")

    def __enter__(self):
        self.request("initialize", {"clientInfo": {"name": "agent-standards-verifier", "version": "1"},
                                    "capabilities": {"experimentalApi": True}})
        self.send({"method": "initialized"})
        return self

    def __exit__(self, *_):
        self.selector.close()
        if self.process.poll() is None:
            os.killpg(self.process.pid, signal.SIGTERM)
        try:
            self.process.wait(timeout=5)
        except subprocess.TimeoutExpired:
            os.killpg(self.process.pid, signal.SIGKILL)
            self.process.wait(timeout=5)


def project_hook_source(project: pathlib.Path) -> pathlib.Path:
    """Codex reads linked-worktree hook declarations from the primary checkout."""
    result = subprocess.run(["git", "rev-parse", "--git-common-dir"], cwd=project,
                            capture_output=True, text=True, timeout=5)
    if result.returncode:
        return project / ".codex/hooks.json"
    common = (project / result.stdout.strip()).resolve()
    return common.parent / ".codex/hooks.json" if common.name == ".git" else project / ".codex/hooks.json"


def inspect(server: AppServer, project: pathlib.Path, trust: bool, dry_run: bool):
    generated = project / ".codex/hooks.json"
    hook_source = project_hook_source(project)
    if hook_source != generated and generated.is_file():
        if not hook_source.is_file() or hook_source.read_bytes() != generated.read_bytes():
            raise ValueError(f"{project}: Codex reads linked-worktree hooks from {hook_source}; land matching reviewed declarations there before runtime verification")
    if trust:
        manifest = project / ".codex/agent-standards/manifest.json"
        if not manifest.is_file():
            raise ValueError(f"{project}: no reviewed generated delivery manifest")
        for relative, item in json.loads(manifest.read_text())["files"].items():
            parsed = pathlib.PurePosixPath(relative)
            if parsed.is_absolute() or ".." in parsed.parts or not (relative.startswith(".codex/") or relative == "AGENTS.override.md"):
                raise ValueError(f"{project}: unsafe generated manifest path")
            path = project / relative
            if not path.resolve().is_relative_to(project.resolve()):
                raise ValueError(f"{project}: generated manifest path resolves outside checkout")
            actual = str(path.readlink()).encode() if item["link"] else path.read_bytes()
            if hashlib.sha256(actual).hexdigest() != item["sha256"]:
                raise ValueError(f"{project}: changed generated content requires reconciliation: {relative}")
        source = pathlib.Path(__file__).resolve().parents[1] / "templates/codex-hooks.py"
        if (project / ".codex/hooks/agent_standards.py").read_bytes() != source.read_bytes():
            raise ValueError(f"{project}: dispatcher does not match the reviewed canonical source")
    hooks = server.request("hooks/list", {"cwds": [str(project)]})
    skills = server.request("skills/list", {"cwds": [str(project)], "forceReload": True})
    approvals = []
    for entry in hooks["data"]:
        if entry["errors"]:
            raise ValueError(f"{project}: hook discovery errors {entry['errors']}")
        for hook in entry["hooks"]:
            if pathlib.Path(hook["sourcePath"]) != hook_source:
                continue
            command = hook.get("command", "")
            expected = 'python3 -B "$(git rev-parse --show-toplevel)/.codex/hooks/agent_standards.py" # agent-standards-content-sha256:'
            if not command.startswith(expected) or len(command.removeprefix(expected)) != 64:
                raise ValueError(f"refusing to approve unfamiliar project hook: {hook['key']}")
            if trust and hook["trustStatus"] not in {"trusted", "managed"}:
                approvals.append({"key": hook["key"], "hash": hook["currentHash"]})
                if not dry_run:
                    server.request("config/value/write", {
                        "keyPath": "hooks.state." + json.dumps(hook["key"]) + ".trusted_hash",
                        "value": hook["currentHash"], "mergeStrategy": "upsert"})
    if approvals and not dry_run:
        hooks = server.request("hooks/list", {"cwds": [str(project)]})
    if generated.is_file():
        expected = {name[0].lower() + name[1:] for name in json.loads(generated.read_text())["hooks"]}
        actual = [hook for entry in hooks["data"] for hook in entry["hooks"] if pathlib.Path(hook["sourcePath"]) == hook_source]
        observed = {hook["eventName"] for hook in actual}
        if expected - observed:
            raise ValueError(f"{project}: missing native hooks {sorted(expected - observed)}; check checkout trust")
        if any(not hook["enabled"] for hook in actual):
            raise ValueError(f"{project}: generated native hooks were explicitly disabled; preserved that setting")
        if not (trust and dry_run) and any(hook["trustStatus"] not in {"trusted", "managed"} for hook in actual):
            raise ValueError(f"{project}: generated native hooks still require reviewed trust")
    errors = [error for entry in skills["data"] for error in entry.get("errors", [])]
    if errors:
        raise ValueError(f"{project}: native skill discovery errors {errors}")
    discovered = [skill for entry in skills["data"] for skill in entry["skills"]]
    native = {pathlib.Path(skill["path"]).resolve() for skill in discovered if skill.get("enabled") is True}
    missing = [str(path.relative_to(project)) for path in (project / ".agents/skills").glob("*/SKILL.md") if path.resolve() not in native]
    if missing:
        raise ValueError(f"{project}: native skills were not discovered: {missing}")
    return {"project": str(project), "hooks": hooks, "skills": skills, "approvals": approvals}


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--project", type=pathlib.Path, action="append", required=True, help="Checkout to inspect; repeat for several")
    parser.add_argument("--trust-reviewed-hooks", action="store_true", help="Record native trust for reviewed generated hook declarations")
    parser.add_argument("--dry-run", action="store_true", help="Report trust handshakes without recording them")
    args = parser.parse_args()
    root = pathlib.Path(__file__).resolve().parents[1]
    folder = root / "tmp/logs"
    folder.mkdir(parents=True, exist_ok=True)
    stamp = datetime.datetime.now(datetime.timezone.utc).strftime("%Y%m%dT%H%M%S%f")
    log = folder / ("codex-runtime-" + stamp + ".jsonl")
    failed = False
    with log.open("w") as output, log.with_suffix(".stderr.log").open("w") as errors:
        def record(level, event, **fields):
            output.write(json.dumps({"timestamp": datetime.datetime.now(datetime.timezone.utc).isoformat(),
                                     "severity": level, "event": event, **fields}) + "\n")
            output.flush()
        started = time.monotonic()
        try:
            with AppServer(args.project[0].resolve(), errors) as server:
                for project in args.project:
                    result = inspect(server, project.resolve(), args.trust_reviewed_hooks, args.dry_run)
                    record("INFO", "codex.discovery", **result)
                    record("INFO", "codex.discovery.summary", project=str(project),
                           hooks=[{"event": hook["eventName"], "trust": hook["trustStatus"], "enabled": hook["enabled"]} for entry in result["hooks"]["data"] for hook in entry["hooks"]],
                           skills=[{"name": skill["name"], "path": skill["path"], "enabled": skill["enabled"]} for entry in result["skills"]["data"] for skill in entry["skills"] if str(project) in skill["path"]],
                           skill_errors=[error for entry in result["skills"]["data"] for error in entry.get("errors", [])])
        except Exception as error:
            record("ERROR", "codex.discovery.failed", error=str(error), stack=traceback.format_exc())
            failed = True
        record("ERROR" if failed else "INFO", "codex.discovery.finished", duration_seconds=time.monotonic()-started, status=1 if failed else 0)
    print(log)
    return 1 if failed else 0


if __name__ == "__main__":
    raise SystemExit(main())
