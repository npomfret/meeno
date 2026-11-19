---
name: application-design
description: Structure of application code. Use when adding or changing behaviour in classes, modules, services or functions, refactoring, introducing an external dependency (network, storage, clock, randomness), deciding where logic lives, or making code unit-testable, even if design is not mentioned. Covers injection, composition root, immutability, encapsulation, domain types and globals. Not for UI markup, SQL, scripts or async workflow design.
---

# Application design

Use with `stateful-workflows` when behaviour changes over time, `runtime-configuration` when settings are read, and `ui-implementation` for screens.

Construct dependencies at the program's entry point and pass each consumer the specific collaborators and configuration it uses. Static application state is not allowed.
### Prefer

- Required constructor parameters for classes and explicit parameters for functions. Constructed objects are ready to use; per-call data belongs in method arguments.
- Narrow, application-owned interfaces for external capabilities such as network, storage, time, randomness and scheduling. Put unavoidable platform globals behind injected adapters.
- Runtime-owned instances with a defined lifetime, shared by passing references. Use an injected narrow factory when construction depends on later request/session data.
- Immutable values and fields, including owned collections and nested data. Use `const` in JavaScript/TypeScript, `let` in Swift, or the language's equivalent; an immutable reference alone does not protect its contents.
- Minimal private mutable state with a behavioral reason, one owner and a validity/invalidation policy. Keep transitions consistent across failure, cancellation and asynchronous work; prevent stale work from overwriting newer state.
### Encapsulate state and behaviour

Keep state together with the behaviour that queries it, changes it and preserves its invariants. Keep representation and internal workflow private, especially where state is mutable. Let objects tell callers meaningful outcomes through intent-based operations and immutable observations.

- Callers must not manipulate internal state or coordinate the private steps needed to maintain an object's validity. Avoid general getters and setters that merely expose representation.
- Rarely expose internal fields on behaviour-owning objects, even when the fields are immutable. Prefer operations that answer meaningful questions or perform domain actions. Replacing a field with an equivalent getter does not improve encapsulation. Immutable value objects and boundary data-transfer objects may expose data where that is their purpose.
- Do not expose mutable collections, nested objects or intermediate state through aliases. Do not expose internals solely for tests.
- Review abstractions by asking what callers no longer need to know. Moving code into a helper or forwarding interface does not establish ownership of behaviour or invariants.
- Helpers are often a sign that an object lacks behaviour it should own. Inspect the state and invariants involved and move behaviour to its cohesive owner where appropriate.
- More than four instance fields is a design smell that triggers a responsibility review. Ask whether the class combines distinct concepts and whether state and behaviour should be separated into two cohesive encapsulations. Use this as a diagnostic heuristic and split according to real responsibilities.
- There is no upper limit on the number of behaviour methods a class may have. Many cohesive operations can belong to one abstraction. Review complexity through the amount of owned state and the relationships and invariants it must maintain.
### Immutability by default

Immutability is mandatory by default. Initialise fields during construction and use the language's readonly, final, let or equivalent mechanism to prevent reassignment. Injected collaborator references and supplied configuration must remain immutable for the consumer's lifetime.

- Prefer immutable domain values and return a new value when data changes. Owned collections and nested data must also be protected from mutation through constructor inputs, aliases and returned references. Use immutable representations, defensive copies or appropriately owned read-only views. A read-only view alone cannot prevent mutation by another holder of its underlying data.
- Every mutable field requires a concrete behavioural reason, one explicit owner, a defined lifetime and valid transitions. Keep mutable state minimal and private. Derive values where possible instead of storing redundant state that can drift.
- Keep related state changes consistent across failure, cancellation and partial completion. Control concurrent access and preserve invariants across asynchronous suspension points. Prevent stale work from overwriting newer state.
- Tests construct independent instances and exercise meaningful transitions through public behaviour. Do not expose mutable internals or add general setters to make tests convenient.
### Use strong domain types at boundaries

In type-safe languages, use the type system to express domain meaning and enforce valid states throughout the application. Every component has boundaries where values enter or leave its responsibility. Validate and encapsulate incoming values at those boundaries, then carry the resulting domain types through internal interfaces.

- Convert user input, deserialised messages, database results and third-party data into strongly typed domain values as soon as they enter the application. These are illustrative boundaries. Apply the same rule wherever raw representations enter a component.
- Represent meaningful domain concepts with dedicated types that own their validation and invariants. For example, text representing a person's name becomes a name value. Give domain identifiers, quantities and states their appropriate types so the compiler can prevent accidental interchange and invalid combinations.
- Internal application operations receive and return domain types. Keep raw strings, numbers and booleans used as transport or storage representations at their owning boundaries. Primitive calculations and representation details belong inside the types and operations that own their meaning.
- Translate domain values into transport or storage representations when crossing an outgoing boundary. At the receiving boundary, validate and reconstruct domain values before invoking application behaviour. A typed sender does not establish the validity of received data.
- Construct valid domain objects from database results before passing them into application behaviour. Apply the same discipline to UI input and external API payloads.
- Preserve domain types across collaborators and call chains. Do not discard their guarantees through loose shapes, unchecked casts or broad primitive parameters. Runtime validation establishes trust in external data, and the type system carries that guarantee inward.
### Separate construction from behaviour

Separating construction from behaviour is mandatory. The composition root assembles the application near its entry point. It reads and validates configuration, selects implementations, constructs collaborators and owns their lifetimes. Behaviour code receives ready-to-use collaborators and performs application work through their interfaces.

- Use constructor injection for objects and explicit parameters for functions. Required collaborators must be supplied when the consumer is constructed. Keep per-call data in method arguments.
- Keep configuration loading, provider selection and dependency wiring in the construction layer. Use builders or factories where they clarify that construction.
- When construction requires data available later, inject a narrow factory assembled by the construction layer. Behaviour code supplies the per-call data to that factory. The factory owns construction and returns a ready-to-use object.
- Apply this separation at each relevant boundary. A constructed object can receive an injected factory for a later scope while keeping its own behaviour independent of construction details.
- Keep behaviour directly constructible in unit tests with in-memory collaborators. Tests must be able to exercise it without starting the application, loading production configuration or contacting external systems.
- Do not construct external dependencies inside behaviour code, resolve them through a service locator or pass a general dependency container inward. Creating ordinary local values and domain objects remains valid.
### Design for fast, reliable unit tests

Abstract external capabilities as soon as they are introduced. Assume each dependency will need replacing soon and make that replacement local to its adapter and construction. Identify capabilities that introduce latency, shared state or failures outside the code's control, including capabilities built into the language or platform. The examples below are illustrative and apply only where the project uses the capability.

- Define a narrow, project-owned interface exposing only the operations the application needs. Keep provider clients, types, configuration and quirks inside the adapter and composition root.
- Make the abstraction own the policy and invariants required by the application. Keep implementation access private so consumers cannot bypass those guarantees.
- Dependency injection is mandatory for external capabilities. Inject the constructed collaborator through its project-owned interface into behaviour code. Consumers must not receive connection parameters or other construction configuration to create that dependency themselves. Construct it at the composition root and pass it inward. Production wiring supplies the real adapter, and unit tests supply an in-memory fake, stub or mock through the same interface.
- Make relevant behaviour controllable through that interface so tests can exercise outcomes immediately and reliably. Substitutes preserve the contract, including ordering, cancellation and failure semantics where applicable.
- Give each test independent substitute state. Keep ordinary behavioural coverage in memory and verify real external mappings with integration tests.
- Judge the boundary by what consumers no longer need to know and how much behaviour can be tested quickly and reliably in memory. Replacing a provider should primarily change its adapter, wiring and configuration.

For example, a time-dependent operation can receive a clock or scheduler whose test substitute advances simulated time or triggers callbacks immediately. A database adapter can expose a few application operations from a much larger driver API and enforce transactions where the application requires them. These illustrate controllability, a reduced interface and encapsulated policy.
### Avoid

- Singletons, module/static service instances or configuration, global accessors, service locators and global configure/reset lifecycles.
- Hidden construction through default dependencies or property injection, and passing a general dependency container inward.
- Reading configuration in behavior code or at module import time. The entry point reads and validates its explicitly supplied configuration files once into typed immutable configuration, then passes only the needed settings inward.
- Copying an existing global-state pattern into new code. Removing existing state is separate work unless requested or necessary for the task.

Immutable constants, stateless functions and deterministic value construction are allowed; service instances and runtime configuration are not constants. If a change would require new static application state, explain the missing construction boundary instead of adding a stopgap.

Tests construct units with in-memory configuration and fakes. Independent instances must coexist without interference. Exercise meaningful state transitions through behavior; do not supply dependencies by patching modules, changing the environment or resetting globals.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
