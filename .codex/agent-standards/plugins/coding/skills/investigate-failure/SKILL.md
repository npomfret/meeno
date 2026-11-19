---
name: investigate-failure
description: Finding why something fails. Use for bugs, crashes, errors, wrong output, regressions, flaky tests or builds, "why does this happen", "it stopped working" and failures that happen only sometimes, before changing code to fix it. Also for collecting audit logs from apps, servers, devices or CI, and for writing or updating a troubleshooting guide. Not for planned feature work.
---

# Investigate failure

For evidence collection or reproduction that can run apart from the parent task, spawn a `failure-investigator` subagent (Codex: `spawn_agent`; Claude Code: the Agent tool) and wait for its report; the parent still applies the standards to the fix.

### Troubleshooting and audit-log collection

Every project must have a maintained, discoverable troubleshooting guide, provided as a document or a clearly described skill. Use simple, factual instructions with runnable commands explaining how to obtain evidence and diagnose failures. Include observed failure patterns when useful.

- Every guide must explain how to collect audit logs. Provide maintained scripts or command targets for every applicable source, including local applications, deployed servers, clients, connected devices and TeamCity builds. Reuse existing collection tooling where it provides the required capability.
- Make collection parameterised. Let the caller select the relevant environment, server, client, device or build through explicit arguments. Document those arguments and where the resulting evidence is written. Server and device examples illustrate this rule and apply where the project uses them.
- Save collected audit logs and diagnostic artifacts under the project's gitignored root `tmp/` directory, normally `tmp/logs/`, grouped by collection and source so evidence from different runs or participants can be correlated. Keep collection scripts in maintained, version-controlled project locations.
- Document the actual audit-log sources and formats, access requirements, exact collection commands and useful ways to inspect the results. Include all applicable participants in multi-client failures. Preserve timestamps, severity, structured fields, stacks and cause chains.
- For TeamCity, use the shared TeamCity tooling to obtain build audit logs and downloadable test artifacts. A project without a particular deployment, device or CI integration needs no invented collection target for it.
- Collection must be non-interactive for routine authorised access, bound network operations with explicit timeouts, report missing access or unavailable sources clearly, and retain successfully collected evidence if another source fails. Apply the scripting, permissions and audit-log redaction standards.
- When investigating, collect evidence, reproduce the failure and test one explicit hypothesis at a time. Remove unsuccessful experiments before trying another. Update the guide when the investigation establishes a durable diagnostic procedure or failure pattern.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
