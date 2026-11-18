

## Git

This workflow applies to every owned project, independently of the active tooling
roster. It supersedes older instructions requiring feature branches or reserving
ordinary fast-forward landing and pushing for a human. Here `main` means the
repository's actual default branch when it has a legacy name such as `master`;
never create a branch merely to satisfy the name.

Work on `main`. Do not create or work on another branch unless explicitly instructed by the user. Work is complete only when its verified, reviewed commits have landed on `main` and been pushed to GitHub. A local commit, worktree result or open PR is not completion. Report any landing or push blocker explicitly.

Protect other agents' work. The shared checkout stays on its default branch; isolated work uses detached worktrees instead of feature branches.

- GitHub repositories must be private unless the user explicitly instructs otherwise.
- Write an appropriate commit message that identifies the change and its purpose. Substantial work must have an accompanying, updated task file following the task-file workflow. That task file can carry detailed rationale, decisions and verification in place of a lengthy commit message.

- When pulling, use `git pull --rebase`, not a merge. Resolve rebase conflicts commit by commit.
- Land rebased worktrees with `git merge --ff-only`. Agents are responsible for landing their own work.
- Do not stash, discard or commit someone else's changes. If their work blocks an operation, use a worktree, wait or report the blocker.
- Commit, land and push scoped, verified work after required review without asking again, unless the user requests otherwise for the task.
- Do not force-push the default branch.

### Worktrees

Use a worktree when the checkout contains others' changes or the work needs more than one commit. Otherwise work directly on the default branch and stage only your files.

Create detached worktrees with `git worktree add --detach tmp/worktrees/<name> main`. Use the shared checkout's root `tmp/` directory for worktree isolation. Do not create feature branches or switch branches in the shared checkout unless explicitly instructed.

Build, test and commit inside the worktree. A subagent commits there and returns the commit ID; its parent remains responsible for landing and pushing it.

### Temporary files

Every project must have a `tmp/` directory at its root, excluded from Git by its root `.gitignore`. Put task-generated files that are not intended for version control there, including scratch files, temporary scripts, logs, captures, reports and detached worktrees. Keep committed source, documentation and task files in their maintained project locations.

### Landing

1. Commit the worktree's changes.
2. Confirm the shared checkout is on the default branch; otherwise stop and report it. Update it with `git pull --rebase`.
3. Rebase the worktree onto the updated default branch. Get its new commit ID with `git rev-parse HEAD`, then land that ID from the shared checkout with `git merge --ff-only <commit-id>`. Rebase again if the default branch moved.
4. Push the default branch and confirm the remote contains the landed commits. If the push is rejected, reconcile with the remote using rebase and retry; do not report completion while delivery is blocked.
5. Leave and remove the worktree once its commits have landed and been pushed, or have been deliberately dropped. Remove only worktrees you created; report other apparently stale worktrees without removing them.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
