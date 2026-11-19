---
name: task-files
description: Plan, track and record substantial work in a committed tasks/ file. Use when starting a multi-step feature, migration or investigation, when asked to plan, write up an approach, track something, or record a decision, its evidence or progress so far, when updating a task, and when finished work needs its task completed or deleted. Not for small edits a commit message can explain.
---

Use one committed task file for planned work, and for unplanned work whose problem, solution and evidence need more than a reasonable commit message. Update an existing task when it already owns the work.

Substantial work must have an accompanying, updated task file. An appropriate concise commit message identifies the change and its purpose. The task file can replace a detailed commit message by carrying the rationale, decisions, progress and verification evidence.

- Place `<slug>.md` in the nearest relevant `tasks/` folder. Keep existing useful naming/grouping conventions; other documentation belongs in `docs/`.
- Start with the issue and why it matters. Add approaches, decisions, a plan, observable success criteria, evidence and remaining work as they become useful. Avoid empty headings and chronological diaries; revise the current account in place.
- Update the task alongside its implementation. Mark work complete only after verification passes and its commits have landed on the default branch and been pushed; otherwise record the exact blocker.
- Reference code by file, class, method or other stable symbol name. Do not use line numbers in task files because they drift as code changes.
- Keep references to task files out of code, comments and durable docs: completed task files are deleted. Commit messages may name them.
- When the last work lands, record the result and verification in the accompanying task file. Preserve any durable product/architecture conclusion in `docs/`, then delete the completed task in a separate follow-up commit. Move genuine remaining work to a narrower task first.

An established project tracker, folder or template may serve the same purpose. Record that convention once rather than adding another tracking system.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
