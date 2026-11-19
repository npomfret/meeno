---
name: http-boundaries
description: HTTP clients and servers. Use when adding or changing an endpoint, an API call from an app or script, fetch, axios or URLSession client, request or response contract, compression, content negotiation, or boundary timeouts and retries, including "call this API" and "speed up responses". Not for UI components, database access or queue workers.
---

# HTTP boundaries

Browser auth, timeouts, retries, errors and validation have one transport owner, defined in `web-factoring`; do not create a second one.

### HTTP compression

HTTP client and server boundaries must support negotiated compression. Keep compression and content-negotiation handling in the shared HTTP boundary so callers receive consistent behaviour.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
