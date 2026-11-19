---
name: verification-gates
description: Automated quality gates. Use when adding or changing lint rules, type checks, static analysis, architecture or dependency-boundary checks, formatters or CI checks, or deciding what blocks a commit or build, including "add a lint rule", "make CI fail when" and "lint is blocking me". Substantive failures block; cosmetic style never does. Not for diagnosing why a check fails.
---

### Verification and formatting

Automated gates must protect program correctness, structural soundness and the agreed engineering rules. Compiler and type errors, broken behaviour and violations of those rules are substantive failures.

- Block commits or CI for substantive failures. Cosmetic preferences such as line length, whitespace, casing and formatting must not be blocking lint errors.
- Use static analysis for defects and enforceable contracts, including unsafe behaviour and architecture or dependency-boundary violations. Judge each rule by the failure or engineering requirement it protects.
- Let the project's formatter handle presentation automatically on touched files. Formatting differences alone must not prevent landing work. Avoid repository-wide formatting churn during unrelated tasks.
- Run tests, builds and type checks deliberately, as the agent doing the work; do not wire them into hooks. No test hooks. No lint hooks. A formatting hook is acceptable only when targeted at the changed files. Lint style findings are opinion, not fact, and are never blocking.
- Reviews focus on substantive problems with evidence. Skip cosmetic preferences and nits.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
