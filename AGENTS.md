<!-- agent-standards:begin -->
## Shared agent guidance

Before starting work, read [Scope](.agents/agent-standards/guidance/scope.md), [Running commands](.agents/agent-standards/guidance/commands.md), [Git](.agents/agent-standards/guidance/git.md), [Review before commit](.agents/agent-standards/guidance/review.md).
Before code changes: [State and dependencies](.agents/agent-standards/guidance/dependencies.md); Before tests or timeout changes: [Test timeouts](.agents/agent-standards/guidance/test-timeouts.md).
Keep the shared checkout on its default branch. Use detached worktrees. Agents commit, fast-forward land and push their verified, reviewed work.
- Be concise: lead with the outcome; give only the detail needed to act or decide.
- Do the work properly and verify it; never trade correctness for speed or cost.
- When options exist, recommend the best technical solution first, even if costlier; state cheaper alternatives separately with their trade-offs.
In Codex, use native skills and specialists for equivalent Claude invocations; referenced `.claude/` files remain canonical project knowledge.

*Managed by `npomfret/agent-standards`; edit the shared sources there.*
<!-- agent-standards:end -->
