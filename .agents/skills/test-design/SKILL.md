---
name: test-design
description: Writing, changing or running automated tests. Use for adding coverage, unit, integration or end-to-end tests, test data and builders, fakes and controlled clocks, drivers and page objects, async waits and polling, test timeouts and budgets, flaky or slow tests, and CI failure artifacts, including "cover this", "tests time out" and "increase the timeout". Load it for browser and iOS UI tests too, together with their platform skill. Not for UI driver specifics, which those skills add.
---

# Test design

Use short, explicit limits so slow or stalled work fails promptly. Run local builds and tests one at a time across agents, with one test worker; wait for any test run you control to finish before starting another.

- Protect meaningful behaviour with repeatable automated tests. Exercise rules, non-trivial construction, boundaries and state transitions. Straightforward assignments and forwarding need no dedicated tests. Keep tests fast and reliable so a failure is actionable.
- Production code contains nothing that exists only to serve tests: no test-only branches, flags, hooks, exports, accessors or constructors. Reach the behaviour through its public interface and injected collaborators.
- Use the same test-type definitions across all projects. A unit test is a small, fast, reliable test that runs entirely in memory with no external dependencies. It makes no API calls, database queries, third-party service calls, network requests, filesystem accesses or external-process calls. Supply in-memory substitutes for external capabilities. A unit test may exercise several real collaborating objects or classes together. The number of classes under test does not determine its type. Unit tests are the most important tests, and code should be designed so that as much meaningful behaviour as possible can be covered by them.
- Fixed sleeps followed by an assumption that work completed are forbidden in every test layer. Use a shared poll-until-matcher driver that repeatedly reads fresh state and applies a side-effect-free matcher, returning immediately on a match and failing at a short deadline. Keep polling frequent and bounded without busy-spinning or starving the work being observed. Scheduling between reads belongs inside the driver. State polling only observes and never reissues the initiating action. Where an existing platform policy permits bounded recovery through an idempotent action/assertion pair, keep that recovery explicit in the driver and subject to the same deadline and auditing.
- Every asynchronous test wait must go through this audited driver boundary, including framework retrying assertions, readiness, absence and event-based waits. Build timing audits into the shared infrastructure so individual tests cannot omit them. Emit one fixed event label plus single-line JSON for every wait outcome, including success, timeout, cancellation and unexpected errors. Include stable test/operation identity, expected condition, outcome, elapsed milliseconds and timeout milliseconds. Include attempt count when available and safe last-observed state on failure. Measure elapsed time with a monotonic clock, preserve the original failure and stop pending work at the deadline.
- Retain these audits in CI logs published by TeamCity so timings and timeout frequency can be compared across builds. Successful waits must be recorded too. Avoid logging every polling attempt.
- Set budgets for tests, setup/teardown, driver waits, suites and CI jobs. Use an outer watchdog when the framework cannot express the required limit. In-memory unit tests should take well under a second; UI waits should match an acceptable user response time.
- An integration test exercises application code across a real external boundary. It may query or update a real database, call an API or third-party service, access the network or filesystem, or interact with another process. State which boundaries are real and which use substitutes. Integration tests must still be reliable and use bounded waits.
- An end-to-end test exercises a separately running application through its public interfaces, such as its user interface or public API. Deploy and run the application and its supporting stack as close to production as practical, locally or on a development or staging server. The application code under test runs outside the test runner. Establish scenario data, perform actions and observe results through the interfaces the application is designed to expose. Direct database reads or writes, calls to application internals and inspection of private state are forbidden in end-to-end scenarios.
- Time is an external capability too. Unit tests receive a controllable clock and scheduler where behaviour depends on time, and advance simulated time without waiting for real time to pass. Reading the system clock or depending on real elapsed time to exercise application behaviour belongs in integration coverage. A test runner or timing audit may measure elapsed time without changing the test's classification.
- A timeout stops the stalled work and reports the phase, elapsed time, limit and observed state.
- Calibrate against audited timings on the slowest environment, usually a busy CI agent, with a proportionate margin for load. A limit several times the normal duration can be reasonable. Millisecond operations must not acquire multi-second defaults without evidence. Account for cumulative failure time across thousands of tests and investigate unexpected slowness and flakiness.
- Raise a limit only on measured evidence: record the timings, the new limit and the reason beside that operation. Keep shared defaults unchanged.

### Test drivers and asynchronous behaviour

Use drivers for non-trivial test interactions. A driver exposes meaningful application operations and hides the mechanics of controlling and observing the system. Page objects are UI drivers, and the page object model is mandatory for all UI tests. Server API drivers serve the same purpose for request construction, calls and response handling. These are illustrative applications of the same pattern.

- Tests state scenario data, actions and expected outcomes through driver operations. Drivers own element lookup, input, protocol details, synchronisation and diagnostics. Direct UI element access, locators, selectors, field reads and writes, and asynchronous waiting in test bodies are forbidden.
- Treat UI interactions and their effects as asynchronous, including button clicks, field validation, animation and request completion. Drivers use the shared audited poll-until-matcher infrastructure to read fresh state until the required condition matches or the deadline expires.
- Keep polling and its timing audits inside shared driver infrastructure. Record what each wait expects, its elapsed time, configured limit and outcome, including successful waits. Every driver wait must participate.
- Give drivers cohesive responsibilities and reuse them across scenarios. A change to a field or interaction should have one driver owner to update. Keep scenario-defining inputs and expected outcomes visible in the test.
- Look for test code that obscures the scenario with incidental setup or preparatory mechanics. More than roughly three lines of such preparation is a signal to consider a driver or improve an existing one. Use this as a design heuristic. Keep meaningful scenario data visible and move distracting mechanics behind a cohesive interface.
- Apply the same driver, direct-access prohibition and audited polling rules across UI platforms, including native SwiftUI applications tested through XCUITest. Platform-specific element lookup and input belong inside the UI driver.
- Browser-driven tests, including Playwright tests, are integration tests because they use a real browser as an external dependency. Apply the end-to-end definition when a scenario exercises the separately deployed, production-like stack through public interfaces. Using a browser alone establishes the integration boundary.

### Meaningful assertions and starting state

For every test of a state change, establish and verify the relevant starting state before performing the action, then assert the required outcome afterward. This is especially important in driver-based, page-object and other non-trivial tests where existing state could already satisfy the final assertion. The test must fail if the action does nothing or produces the wrong result.

The starting check may simply establish that the expected final condition is not already true. For example, before changing a name to Bob, assert that the name is not Bob, then perform the action and assert that it is Bob. An exact previous value is required only when that value matters to the scenario.

Use the legitimate public behaviour or driver interface to check state. Keep UI synchronisation and polling inside the driver. Do not expose internals for assertions. Choose preconditions appropriate to unchanged-state and idempotency scenarios, where preserving an existing state is itself the required behaviour.

### Test failure evidence

Prepare for failures whose cause is initially unclear. Applications audit meaningful user actions, API calls and state transitions. Tests provide diagnostic errors, and shared waiting infrastructure records expected conditions, elapsed time, limits and outcomes.

- On failure, collect and publish the relevant application/server logs, browser console logs, test errors and wait timing records as downloadable CI test artifacts. Include screenshots of the failed UI state for UI tests. Configure artifact publication to run even when the test step fails.
- For scenarios with multiple users, browsers, contexts or devices, collect logs and failure screenshots for every participant. Identify the test and participant in artifact names and records so the evidence can be correlated.
- Browser application logs follow the same auditing contract as server logs. Each event has a timestamp, severity, fixed message label and structured JSON data. Preserve browser errors and their diagnostic context alongside those events.
- Failure messages identify the operation, expected condition and actual observed state. Wait failures also report elapsed time and the configured limit. Preserve original errors and stacks.
- Retain wait timing records for successful and failed tests across builds so duration trends and timeout frequency can be analysed. Successful tests do not require routine screenshots or extensive diagnostic artifacts.
- Keep collection in shared test infrastructure and drivers so individual scenarios cannot accidentally omit evidence. Capture available failure evidence before cleanup destroys it, and preserve the original test failure if evidence collection also fails. Apply the auditing redaction rules to published artifacts.
### Test-data builders

Always use builders to construct domain objects in tests. Builders hide incidental data construction while keeping scenario-defining inputs visible. They produce valid objects by default with randomised field values within the valid domain. Each test must explicitly specify all fields necessary to make its scenario pass, and only those fields.

- Randomise incidental fields so tests cannot silently depend on convenient fixed defaults. Supply randomness explicitly through a seedable generator. Record the seed and test identity in failure evidence so the same generated data can be reproduced.
- Tests explicitly fix only fields that establish the scenario's necessary preconditions or expected outcomes. Every override must earn its place. Removing a necessary override should invalidate the intended scenario or cause its meaningful assertions to fail. Remove overrides that do not affect the scenario.
- Randomised defaults help reveal an omitted necessary override but cannot guarantee detection on every run. Preserve valid domain relationships and keep generated values within the intended scenario's incidental variation so randomness does not introduce unrelated invalid cases or uncontrolled flakiness.
- Construct malformed input deliberately at the boundary under test using explicit variations. Do not weaken production types or generate invalid objects accidentally.
- Give each test independent builder state and data. Distinguish in-memory construction from operations that persist data or perform other external effects.
### Application and browser evidence

Audit meaningful user actions, API calls and state transitions so failures can be reconstructed. Browser application logs follow the same contract as server logs, with timestamp, severity, fixed event label and structured JSON data on one line. Preserve errors and diagnostic context.

Shared test infrastructure collects application/server logs, browser console logs, test errors and wait timing records into downloadable CI artifacts when tests fail. UI failures include screenshots, and multi-participant tests include logs and screenshots for each user, browser, context or device with clear participant identity. Publish available evidence even when the test step fails and capture it before cleanup. Preserve the original test failure if artifact collection also fails.

Retain timing records from successful and failed waits across builds. Routine screenshots and extensive diagnostic artifacts are unnecessary for successful tests. Apply the redaction rules to all published evidence.
### Choose the behaviour's test boundary

Keep UI layers thin, with layout and rendering bound to application-owned model state and user interactions forwarded to its operations. Move decisions, validation, calculations, workflow and state transitions into UI-independent code with injected collaborators. Unit-test that behaviour in memory. Focus UI-driven coverage on rendering, bindings and platform interactions that require a real UI. Apply this across web and native frameworks so ordinary behavioural coverage can run without a browser, simulator or desktop session.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
