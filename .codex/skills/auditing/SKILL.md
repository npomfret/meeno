---
name: auditing
description: Project conventions for structured logs and error reporting. Read before changing log calls, diagnostic scripts or error handling, and when events cannot be explained from existing logs.
---

## Auditing

Use structured logs that explain events, actions and state changes to both people and machines.

### Log shape

- Each call has a fixed event label and structured fields: `logger.info('expense-created', { expenseId, amount, currency })`. Runtime values belong in fields, not interpolated or concatenated into the label.
- Emit one line per call, with timestamp, normal log level, label and JSON data. Use consistent field names and include before/after state when useful.
- Record outcomes, including meaningful no-change outcomes. Avoid empty narration such as "about to start".
- Use debug for diagnostic detail, info for meaningful events, warn for handled surprises, and error for failures.

### Logger and errors

- Construct the project-owned logger at the composition root and inject it. Its interface enforces fixed labels, structured fields and error objects where the language permits. Child loggers carry shared request/job context.
- Application events and script diagnostics use that logger and an audit-log file. Operational scripts print only the audit-log path to stdout and return an exit status. Progress, results and diagnostics belong in the audit log. Explicit CLI help remains interface output under the scripting standard.
- Log an error once where it is handled, preserving its type, stack, cause and other useful properties. Expected validation rejections may be warnings without a stack. Let unhandled errors reach the boundary that deals with them.
- Tests assert event labels and fields, not formatted output. Preserve checks that prevent application code bypassing the logger.

### Exception handling and stack traces

Never lose an error's original stack trace. Every unexpected failure must be audited once at the boundary responsible for handling or reporting it, with the original error type, message, stack and complete cause chain. Diagnostics must retain the source locations needed to identify the application's failing code. For transformed, compiled or minified code, retain the platform's source-location metadata needed to resolve those locations, such as source maps or native debug symbols and symbolication information.

Choose severity according to how the application handles the outcome. An exception representing entirely expected normal control flow may omit its stack from the audit log. A recoverable failure, including one handled by an established retry policy, may be audited at warning or another appropriate level while retaining its stack and cause chain. Every error-level failure record must itself include the stack and cause chain so its source locations are available with the reported error. A warning level alone does not justify discarding a failure's stack.

- Rethrow the original error when adding no new meaning. When translating or adding context, preserve the original error as the cause. In TypeScript with the appropriate runtime and ES2022 library support, use the standard Error cause mechanism. Each wrapper and its cause retain their own stacks.
- Pass error objects through the structured logger. Its serializer must explicitly preserve stacks and recursively preserve causes. Serializing an Error as an ordinary JSON object can discard these properties. Logging only a message, a stringified error or an empty object is insufficient.
- Encourage chained errors where supported. Errors may carry structured diagnostic fields describing relevant inputs, operation identity and state at the point of failure. Preserve those fields through translation and into the audit log alongside the original stack.
- Serialize enriched fields and nested error objects recursively, including their own stacks, causes and diagnostic fields, into the audit log record. The same rule applies to errors grouped together by the platform. Handle cyclic references safely so serialization cannot lose the original failure. Redact sensitive values while retaining the diagnostic structure.
- Preserve these diagnostics through asynchronous boundaries, browser logs, process reporting and CI artifacts. Redaction must retain useful stack frames and cause relationships while removing sensitive values.

The TypeScript ES2022 support for Error causes is documented in the [TypeScript 4.6 release notes](https://www.typescriptlang.org/docs/handbook/release-notes/typescript-4-6.html#--target-es2022).

### Test wait timing

Every asynchronous test wait is audited by the shared driver infrastructure, including framework assertions and event waits. Emit a fixed event label plus single-line JSON on success, timeout, cancellation and unexpected error. Record stable test/operation identity, expected condition, outcome, elapsed milliseconds and timeout milliseconds. Add attempt count when available and safe last-observed state on failure. Use a monotonic clock for durations. Keep the original error and its stack when a wait fails.

Publish these records with TeamCity build logs so agents can compare durations and timeout frequency across builds and justify calibrated limits. Record successful waits as well as failures, without per-poll log noise. Individual test authors must not have to add timing logs themselves.

### Application and browser evidence

Audit meaningful user actions, API calls and state transitions so failures can be reconstructed. Browser application logs follow the same contract as server logs, with timestamp, severity, fixed event label and structured JSON data on one line. Preserve errors and diagnostic context.

Shared test infrastructure collects application/server logs, browser console logs, test errors and wait timing records into downloadable CI artifacts when tests fail. UI failures include screenshots, and multi-participant tests include logs and screenshots for each user, browser, context or device with clear participant identity. Publish available evidence even when the test step fails and capture it before cleanup. Preserve the original test failure if artifact collection also fails.

Retain timing records from successful and failed waits across builds. Routine screenshots and extensive diagnostic artifacts are unnecessary for successful tests. Apply the redaction rules to all published evidence.

### Troubleshooting and audit-log collection

Every project must have a maintained, discoverable troubleshooting guide, provided as a document or a clearly described skill. Use simple, factual instructions with runnable commands explaining how to obtain evidence and diagnose failures. Include observed failure patterns when useful.

- Every guide must explain how to collect audit logs. Provide maintained scripts or command targets for every applicable source, including local applications, deployed servers, clients, connected devices and TeamCity builds. Reuse existing collection tooling where it provides the required capability.
- Make collection parameterised. Let the caller select the relevant environment, server, client, device or build through explicit arguments. Document those arguments and where the resulting evidence is written. Server and device examples illustrate this rule and apply where the project uses them.
- Save collected audit logs and diagnostic artifacts under the project's gitignored root `tmp/` directory, normally `tmp/logs/`, grouped by collection and source so evidence from different runs or participants can be correlated. Keep collection scripts in maintained, version-controlled project locations.
- Document the actual audit-log sources and formats, access requirements, exact collection commands and useful ways to inspect the results. Include all applicable participants in multi-client failures. Preserve timestamps, severity, structured fields, stacks and cause chains.
- For TeamCity, use the shared TeamCity tooling to obtain build audit logs and downloadable test artifacts. A project without a particular deployment, device or CI integration needs no invented collection target for it.
- Collection must be non-interactive for routine authorised access, bound network operations with explicit timeouts, report missing access or unavailable sources clearly, and retain successfully collected evidence if another source fails. Apply the scripting, permissions and audit-log redaction standards.
- When investigating, collect evidence, reproduce the failure and test one explicit hypothesis at a time. Remove unsuccessful experiments before trying another. Update the guide when the investigation establishes a durable diagnostic procedure or failure pattern.

### Redaction

Keep secrets, credentials, cookies, signed URLs, environment values and whole raw request/response bodies out of logs. Include only necessary personal data, usually an ID rather than an email. Redact fields in the logger and scrub known secret values from strings, including URLs.

Verify that changed logs explain what happened and retain the evidence needed to diagnose failures.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
