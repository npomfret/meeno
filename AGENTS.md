<!-- agent-standards:begin -->
## Operating contract

*Managed by `npomfret/agent-standards`; detailed workflows are skills.*

- Skills come first: your first action on any task is to invoke, with the Skill tool, every skill whose description matches the request, before reading code or running commands. A task usually matches several (code design, tests, SQL, UI, scripts, logging, CI, commits); load them all, and again when the work moves to a new kind of task. Starting work without them is a mistake, even when the change looks small.
- Scope: do what was asked plus the callers, tests and migrations it needs. Leave unrelated refactors, renames, cleanup, dependency bumps and docs for separate work; suggest them at the end. Never invent commands, paths, field names or APIs; check the source first. Add failure handling only for observed evidence. When readings differ materially, choose the most plausible, state the assumption in your report and proceed. Done means the behaviour works, verification passes, and you report evidence and blockers.
- Gates: only substantive failures block (types, broken behaviour, agreed rules). Cosmetics are autoformatted and never block commits, CI or reviews. Run tests, builds and type checks yourself; no test or lint hooks, and formatting hooks only when targeted at changed files.
- Machine load: one build or test command at a time per machine, across all agents and sessions (wait for any running xcodebuild). Lean on CI for full suites; locally run only the tests your change needs. No load, stress, performance or other CPU-saturating tests without the user's explicit authorisation.
- Other agents share the checkout. Stage only your files; never stash, discard, revert or commit their changes. The shared checkout stays on the default branch; isolate with `git worktree add --detach tmp/worktrees/<name> <default-branch>`.
- Git: work is complete only when verified, reviewed commits are on the default branch and pushed (`git pull --rebase`, land with `git merge --ff-only`). Force-pushing is permitted; prefer `--force-with-lease`. Commit, land and push without asking. Repositories stay private.
- Significant work (behaviour, refactor, tests, config, tooling) is reviewed independently before commit: spawn a project reviewer or `change-reviewer` subagent (`spawn_agent` in Codex, the Agent tool in Claude Code), never report a review you did not delegate; resolve every finding. Tests and review do not replace each other.
- Substantial work needs a committed task file under `tasks/`.
- Run commands yourself unless they need the user's login, password, sudo, device or install. Reuse the project's scripts and `tmp/ad-hoc-scripts/`; put multi-step or quoted shell in a named script there. Hand the user one short command, else a script run as `! tmp/ad-hoc-scripts/<purpose>.sh`. Send long output to `tmp/logs/`; read it with `rg` and bounded excerpts; never paste whole logs.
- Secrets never go in source, arguments, output or logs, or the repo; they live in iCloud Drive, reached through gitignored `secrets/` links. Configuration comes only from explicitly supplied files, never environment, dotenv or ambient login.
- Code: construct dependencies at the entry point and inject them; no globals, singletons or hidden construction; immutable by default; structured logs with fixed labels that preserve error causes and stacks; no sleeps in tests. Raise a test timeout or limit only on measured evidence, recorded beside the operation.
- GitNexus: index with `npx gitnexus analyze --index-only`; never run `gitnexus setup` or a plain `analyze`, which rewrite AGENTS.md and CLAUDE.md.

- Be concise: lead with the outcome; give only the detail needed to act or decide.
- Do the work properly and verify it; never trade correctness for speed or cost.
- When options exist, recommend the best technical solution first, even if costlier; state cheaper alternatives separately with their trade-offs.
- Fail fast: surface errors with their cause; never swallow, mask or silently fall back from them.
- Keep one canonical implementation: migrate callers and delete superseded code instead of adding shims or compatibility layers.
- Question unclear or unsound requests before acting on them; do not comply silently.
In Codex, use native skills and specialists for equivalent Claude invocations; referenced `.claude/` files remain canonical project knowledge.

*Managed by `npomfret/agent-standards`; edit the shared sources there.*
<!-- agent-standards:end -->
