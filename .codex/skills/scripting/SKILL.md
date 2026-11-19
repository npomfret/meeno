---
name: scripting
description: Project conventions for scripts, command targets and CI steps. Read before writing, changing or reviewing shell, Python, Node or other command-line tooling.
---

## Scripting

Keep command-line tooling predictable for people, agents and CI.

### Language and location

- Use shell for glue; use the project's scripting language, or Python's standard library, for data processing and substantial decisions. Keep one scripting language besides shell.
- Keep maintained scripts in `scripts/`, beside the part they serve when appropriate. Register runnable scripts in the project's command list and name agent entry points in `AGENTS.md`.
- Keep uncommitted ad hoc scripts in the gitignored root `tmp/ad-hoc-scripts/`, with descriptive names, purpose and usage. Inspect that directory and the maintained script catalogue before creating new automation. Reuse or extend existing operations.
- Common tasks such as TeamCity queries, releases and audit-log collection must have maintained, checked-in scripts or named project command targets. Where package scripts are used, keep substantial implementation in script files. Promote recurring ad hoc work into the maintained tooling.
- Resolve paths from the script's own location. Generated output belongs under ignored paths. A committed script needs no per-script approval rule.

### Interface

- Provide `--help` with purpose, synopsis, options/defaults and an example. Use long flags and no interactive prompts.
- Exit 0 for success, 1 for failure, and 2 for invalid usage; print invalid-usage help to stderr.
- Provide `--dry-run` for destructive, costly, external or long-running operations. Destruction also requires `--yes`. Make repeat runs safe or resumable.

### Output and failures

- Operational scripts print only their audit-log file path to stdout. Return the exit status to the calling process. Write progress, results, warnings, errors and subprocess output to the audit-log file. Do not stream operational detail to stdout or stderr.
- This keeps large output out of agent context and conserves token quota. Agents inspect the audit-log file with targeted searches and bounded excerpts, rather than reading the entire output into the conversation.
- Operational scripts, including ad hoc scripts, write an audit-log file under the project's root `tmp/logs/`. Use the same auditing contract as the application, with timestamp, severity, fixed event label and structured JSON fields on one line. Preserve relevant subprocess output with the collected evidence.
- Record stage commands, start/end, duration and status. Distinguish nonzero exits, timeouts and signals. Retain original errors, stacks and relevant output so the summary explains the failure and points to its evidence.
- Do not suppress failing checks or move detail onto the console. Use the project's structured logger for audit records. Explicit `--help` output and invalid-usage help remain CLI interface output as specified above.

### Shell

- Start with `#!/usr/bin/env bash` and `set -euo pipefail`. Use shellcheck for substantive defects and agreed engineering rules, and shfmt for automatic presentation. Cosmetic findings must not block commits or CI. A suppression of a substantive finding needs its specific reason beside it.
- Target macOS bash 3.2 and Linux. Quote expansions, use arrays for arguments, and avoid eval or constructed command strings. Use portable forms where BSD/GNU tools differ.
- Use functions with local variables and a `main` entry point. Create temporary files with mktemp under the project's `tmp/`, and clean them with an EXIT trap.

### State and external calls

- Write files atomically and hold a lock while changing shared state.
- Bound network calls with timeouts. Any retry policy has a limit/backoff, and logs each retry.
- Read configuration and secrets from explicitly supplied files selected through command-line path arguments. Do not depend on environment variables, dotenv files or a machine's active login or cloud project. Pass secret contents to external tools through their supported file or stdin interface, never environment variables or command arguments. Keep secret contents out of source, console and audit logs.
- Test meaningful script logic with unit tests or `--selftest`. Verify a relied-on check against a case it must reject. Simple command glue does not need tests that merely repeat its implementation.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
