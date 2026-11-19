#!/usr/bin/env python3
"""PostToolUse hook: send a session to the auditing skill the first time it touches code that handles errors or logs.

The skill's description alone did not load it. Measured 2026-10-23, Claude Code
2.1.306, in fresh clones of super-funmax-music: asked to make a failed
`jump(to:)` in PlayerBackend.swift report what went wrong, cold sessions loaded
auditing 0 times in 3 without this hook and 3 in 3 with it, each time before
the first edit. That is the shape of tasks 393-395 there, whose repairs were
about errors that lost their cause.

There is no folder that holds error handling, so a file qualifies by what it
contains: a catch, a throw, a raise or a logger. That is most code, which is
fine, because it says so only once per context. A marker per session remembers
that it spoke; compaction drops the context, so SessionStart with source
`compact` clears the marker (`--forget`). It stays silent when the transcript
already shows the skill loaded.
"""
import json
import pathlib
import re
import sys
import tempfile

SKIPPED = {"Pods", "DerivedData", "build", "node_modules", "dist", ".git"}
CODE = {".swift", ".ts", ".tsx", ".js", ".mjs", ".cjs", ".py", ".kt", ".java", ".go", ".rs", ".sh"}
HANDLING = re.compile(r"\b(catch|throws?|except|raise|logger|Logger|console\.(error|warn)|os_log)\b")
LOADED = re.compile(r'"skill"\s*:\s*"(auditing:)?auditing"')

NUDGE = """\
You are working on code that handles errors or logs, and this project follows \
the auditing standard. If you have not loaded it in this session, load it now \
with the Skill tool (`auditing:auditing`) before writing or changing any error \
handling, logging or failure reporting. In short: never hide a stack trace \
unless the failure is fully expected; log the error object itself, keeping its \
name, message, stack, cause and extra properties, never its message pasted into \
a string; record an error once, where it is handled; log through the project's \
one typed logger, each line a fixed label plus a JSON blob, at the normal \
levels."""


def marker(event):
    key = event.get("session_id") or "unknown"
    if event.get("agent_id"):
        key += "-" + event["agent_id"]
    return pathlib.Path(tempfile.gettempdir()) / "auditing" / key


def handles_errors(path):
    path = pathlib.Path(path)
    if SKIPPED.intersection(path.parts) or path.suffix not in CODE:
        return False
    try:
        return bool(HANDLING.search(path.read_text(errors="ignore")))
    except OSError:
        return False


def skill_loaded(transcript):
    try:
        with open(transcript, errors="ignore") as f:
            return any(LOADED.search(line) for line in f)
    except (OSError, TypeError):
        return False


def main():
    try:
        event = json.load(sys.stdin)
    except ValueError:
        return
    spoken = marker(event)
    if "--forget" in sys.argv:
        spoken.unlink(missing_ok=True)
        return
    path = (event.get("tool_input") or {}).get("file_path")
    if not path or spoken.exists() or not handles_errors(path):
        return
    spoken.parent.mkdir(parents=True, exist_ok=True)
    spoken.touch()
    if skill_loaded(event.get("transcript_path")):
        return
    print(json.dumps({
        "hookSpecificOutput": {"hookEventName": "PostToolUse", "additionalContext": NUDGE}
    }))


if __name__ == "__main__":
    main()
