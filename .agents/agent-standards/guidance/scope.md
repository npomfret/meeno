## Scope

Complete the requested work and verify it. Include the dependent changes needed to make it work, such as affected callers, tests or migrations.

- Use the request and existing project requirements as the boundary. Leave unrelated refactors, renames, cleanup, dependency bumps and documentation changes for separate work.
- Add special handling or tests for a failure scenario when there is evidence for it: observed data, an incident or a reproducible failure.
- Suggest adjacent work briefly at the end of the report; continue the current task. An unanswered suggestion is not approval.
- Ask when different interpretations would lead to materially different work. If a necessary change exceeds the authorized scope, explain the concrete dependency and wait for a decision.
- Finish when the requested behavior works and its verification passes. Report what changed, the evidence, and any remaining blocker.

### Verification and formatting

Automated gates must protect program correctness, structural soundness and the agreed engineering rules. Compiler and type errors, broken behaviour and violations of those rules are substantive failures.

- Block commits or CI for substantive failures. Cosmetic preferences such as line length, whitespace, casing and formatting must not be blocking lint errors.
- Use static analysis for defects and enforceable contracts, including unsafe behaviour and architecture or dependency-boundary violations. Judge each rule by the failure or engineering requirement it protects.
- Let the project's formatter handle presentation automatically on touched files. Formatting differences alone must not prevent landing work. Avoid repository-wide formatting churn during unrelated tasks.
- Reviews focus on substantive problems with evidence. Skip cosmetic preferences and nits.
