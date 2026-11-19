---
name: stateful-workflows
description: Behaviour that changes over time. Use for state machines, async or concurrent operations, cancellation, retries, duplicate or late results, idempotency, queues, background jobs, resumable work, caches and invalidation, stale responses and race conditions, including "handle double clicks", "retry on failure", "add a cache" and "fix this race". Not for stateless functions or simple CRUD.
---

# Stateful workflows

### State machines, concurrency and cancellation

Model stateful and asynchronous workflows with explicit states, permitted transitions and one owner responsible for enforcing them. Use the language's types to represent valid states and prevent invalid combinations. Keep state machines visible in the model and its operations so concurrency rules can be understood and tested.

- Prefer immutable values and stateless behaviour. Introduce mutable in-memory state only for a concrete requirement, following the immutability rules. Keep required workflow state private to its owner.
- Define the events and conditions that permit each transition, including success, failure, cancellation and partial completion. Keep related changes consistent and prevent callers from observing invalid intermediate state.
- Define how overlapping operations interact. Identify which operation owns a result and reject stale completions that would overwrite a newer state. Cancellation must leave a valid state and prevent cancelled work from later applying an obsolete transition.
- State whether each operation is idempotent. Repeated requests or deliveries must preserve that contract. Retry only under an established policy, with duplicate protection where repeating an operation could repeat its effects.
- Where workflow state is persisted, commit the transition and associated database changes transactionally. Use the database's concurrency controls and constraints to prevent conflicting updates. A transaction alone does not make an operation idempotent.
- Test meaningful transitions and relevant overlaps, duplicate delivery, late completion, failure and cancellation through public behaviour with controlled collaborators.
### Caches

- Caches require an explicit validity and invalidation policy. Mutation for convenient wiring, later initialisation or test setup is forbidden.
- Every cache must be configurable through runtime-supplied configuration, including whether it is enabled. Each project decides its caching policy and relevant freshness, expiry and capacity settings. Support disabling caching for environments such as development. Read and validate the supplied configuration at the entry or composition boundary and inject the relevant immutable settings into the cache owner. Cache behaviour must not discover global configuration or hard-code an environment-specific policy.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
