---
name: review-changes
description: Independent review of finished work before it is committed. Use for "review this", "look over my changes", "is this ready", a second opinion on a diff, branch or PR, and before committing any behaviour, refactor, test, configuration or tooling change. Not for typo, formatting or task-file edits, and not a substitute for running tests.
---

# Review changes

Delegate the review to a project reviewer agent or to `change-reviewer`; the implementing agent stays responsible for resolving findings. Where the harness only delegates on instruction (Codex), instruct the delegation explicitly.

Have significant work independently reviewed by an agent before committing. This includes behavior, refactors, tests, configuration and tooling; trivial wording, formatting and task-file edits need no review.

- Use an appropriate project reviewer, or a general reviewer. Give it the success criteria, file list/revision range and the actual diff.
- Ask for substantive problems with evidence: bugs, broken contracts, missed requirements, rule violations and ineffective tests. Skip style preferences and nits.
- Resolve each finding or explain why it is incorrect before committing. Report the review result and how findings were handled.

Passing tests do not replace review.

## Rule sources by change type

The reviewer loads the applicable skill before judging, by what the diff touches: tests (`test-design`, plus `browser-testing` or `ios-ui-testing`), UI (`ui-implementation`, `web-factoring`), SQL or schema (`database`), logging or error paths (`auditing`), scripts (`scripting`), configuration or secrets (`runtime-configuration`), HTTP (`http-boundaries`), async or cached state (`stateful-workflows`), structure and dependencies (`application-design`), CI (`teamcity`), gates and lint (`verification-gates`).

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
