#!/usr/bin/env python3
"""SessionStart hook: tell the agent WebStorm can answer for this project, if it can.

Claude Code defers MCP tools behind ToolSearch, so a connected server whose tools
nobody names is a server nobody calls. Measured on a TypeScript project with the
tools granted: asked who calls one function, a session with no hint used Bash and
Read (9 turns, 29s); the same question with a hint went search_symbol ->
analyze_calls (6 turns, 21s) and drew the same tree.

The hint is only true while WebStorm is running with this project open, so it is
given only then. The script asks the server itself, the way a session would,
and says nothing if the server is down, the project is not open, or anything
else goes wrong: an agent pointed at tools that are not there wastes turns on
them. The URL comes from the project's .mcp.json, which sync.py writes, so there
is one place that names the port.
"""
import json
import os
import sys
import urllib.request

SERVER = "webstorm"
TIMEOUT = 2

GUIDANCE = """\
WebStorm has this project open, and its MCP server is connected as `mcp__webstorm`. \
It resolves code the way the IDE does - through imports, types and re-exports - so \
prefer it to grep and file reads whenever the question is about a symbol:

- Where something is defined, or what it is: `search_symbol`, then `get_symbol_info`.
- Who calls a function, or what it calls: `analyze_calls` (INCOMING_CALLS or OUTGOING_CALLS).
- Renaming a symbol: `rename_refactoring`, never a text replacement.
- After an edit: `get_file_problems` or `lint_files`, for the IDE's own inspections.

Grep is still right for strings, comments, config and anything that is not a symbol. \
The tools are deferred, so load them first, e.g. ToolSearch \
"select:mcp__webstorm__search_symbol,mcp__webstorm__analyze_calls,mcp__webstorm__get_symbol_info". \
The server already knows the project; pass `projectPath` = `{root}` only if a call \
says it cannot tell which project you mean. Where another code-intelligence server \
could answer the same question - finding a symbol, its callers, a rename - prefer \
WebStorm, which resolves it through the IDE's own index. Fall back to the other for \
what WebStorm does not cover, such as a language it does not analyse."""


def project_root():
    try:
        event = json.load(sys.stdin)
    except ValueError:
        event = {}
    return os.environ.get("CLAUDE_PROJECT_DIR") or event.get("cwd") or os.getcwd()


def server_url(root):
    try:
        with open(os.path.join(root, ".mcp.json")) as f:
            return json.load(f)["mcpServers"][SERVER]["url"]
    except (OSError, ValueError, KeyError, TypeError):
        return None


def serves(url, root):
    """True when the server answers a project-scoped call for this checkout."""
    # The sandbox advertises its proxy in HTTP_PROXY; a request to localhost must
    # not be sent through it, so no proxy handler at all.
    opener = urllib.request.build_opener(urllib.request.ProxyHandler({}))
    headers = {
        "Content-Type": "application/json",
        "Accept": "application/json, text/event-stream",
        "IJ_MCP_SERVER_PROJECT_PATH": root,
    }

    def post(body):
        request = urllib.request.Request(url, json.dumps(body).encode(), headers)
        with opener.open(request, timeout=TIMEOUT) as response:
            if response.headers.get("Mcp-Session-Id"):
                headers["Mcp-Session-Id"] = response.headers["Mcp-Session-Id"]
            text = response.read().decode()
        for line in text.splitlines():
            if line.startswith("data:"):
                text = line[5:]
        return json.loads(text) if text.strip() else None

    post({
        "jsonrpc": "2.0", "id": 1, "method": "initialize",
        "params": {
            "protocolVersion": "2025-06-18", "capabilities": {},
            "clientInfo": {"name": "webstorm-session-context", "version": "1"},
        },
    })
    post({"jsonrpc": "2.0", "method": "notifications/initialized"})
    reply = post({
        "jsonrpc": "2.0", "id": 2, "method": "tools/call",
        "params": {"name": "get_repositories", "arguments": {}},
    })
    try:
        opener.open(urllib.request.Request(url, headers=headers, method="DELETE"), timeout=TIMEOUT)
    except Exception:
        pass
    result = (reply or {}).get("result") or {}
    return bool(result) and not result.get("isError")


def main():
    root = project_root()
    url = server_url(root)
    try:
        if not url or not serves(url, root):
            return
    except Exception:
        return
    print(json.dumps({
        "hookSpecificOutput": {
            "hookEventName": "SessionStart",
            "additionalContext": GUIDANCE.replace("{root}", root),
        }
    }))


if __name__ == "__main__":
    main()
