## Running commands

Run commands yourself unless they require the user's login, password, sudo, device or installation action. Load the `scripting` skill before writing a script the project keeps.

- Before creating command-line automation, inspect the project's maintained script catalogue and `tmp/ad-hoc-scripts/` for an existing operation to reuse or extend.
- Do not embed a shell program in a complex command line. For multi-step work involving control flow, substantial quoting, orchestration or repeated operations, write a named executable script under `tmp/ad-hoc-scripts/` and run it. This applies to commands agents execute themselves as well as commands handed to the user. Give the script a clear purpose and usage so another agent can discover and reuse it.
- When the user must run a command, give them one short command. For anything multiline, chained or longer than about 80 characters, write an executable `tmp/ad-hoc-scripts/<purpose>.sh` with `#!/usr/bin/env bash`, `set -euo pipefail`, a purpose comment and logging under `tmp/logs/`. Show it, ask them to run `! tmp/ad-hoc-scripts/<purpose>.sh`, then read its log.
- Keep lengthy build, test, install and data output in `tmp/logs/`. Read relevant portions and report the useful lines with the log path. Do not enable verbose/debug output merely to watch a run.
- Large command output consumes agent context and token quota. Keep it in audit-log files and use targeted searches such as `rg` or `grep`, followed by bounded excerpts, to find the evidence needed. Report concise findings and the audit-log path. Do not load or paste entire large audit logs into the conversation.

### Troubleshooting and audit-log collection

Every project must have a maintained, discoverable troubleshooting guide, provided as a document or a clearly described skill. Use simple, factual instructions with runnable commands explaining how to obtain evidence and diagnose failures. Include observed failure patterns when useful.

- Every guide must explain how to collect audit logs. Provide maintained scripts or command targets for every applicable source, including local applications, deployed servers, clients, connected devices and TeamCity builds. Reuse existing collection tooling where it provides the required capability.
- Make collection parameterised. Let the caller select the relevant environment, server, client, device or build through explicit arguments. Document those arguments and where the resulting evidence is written. Server and device examples illustrate this rule and apply where the project uses them.
- Save collected audit logs and diagnostic artifacts under the project's gitignored root `tmp/` directory, normally `tmp/logs/`, grouped by collection and source so evidence from different runs or participants can be correlated. Keep collection scripts in maintained, version-controlled project locations.
- Document the actual audit-log sources and formats, access requirements, exact collection commands and useful ways to inspect the results. Include all applicable participants in multi-client failures. Preserve timestamps, severity, structured fields, stacks and cause chains.
- For TeamCity, use the shared TeamCity tooling to obtain build audit logs and downloadable test artifacts. A project without a particular deployment, device or CI integration needs no invented collection target for it.
- Collection must be non-interactive for routine authorised access, bound network operations with explicit timeouts, report missing access or unavailable sources clearly, and retain successfully collected evidence if another source fails. Apply the scripting, permissions and audit-log redaction standards.
- When investigating, collect evidence, reproduce the failure and test one explicit hypothesis at a time. Remove unsuccessful experiments before trying another. Update the guide when the investigation establishes a durable diagnostic procedure or failure pattern.
