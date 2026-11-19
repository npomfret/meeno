---
name: runtime-configuration
description: How code gets settings, credentials and identity. Use when adding a setting or flag, connecting to a service, reading an API key, service account or endpoint, touching .env or environment variables, choosing which account or project is used, or handling secrets. Applies to servers, scripts and native or client apps. Not for TeamCity token handling.
---

# Runtime configuration

### Explicit file-based configuration

Projects must coexist independently on the same machine. Application and script configuration must never depend on environment variables, dotenv files, machine-wide settings, a user's active login or an implicitly selected account or cloud project.

- Supply configuration files explicitly through one or more command-line arguments at the application or launcher boundary. Apply this to server, client and native application projects. Validate the selected files once and construct typed immutable configuration at the entry or composition boundary.
- Select project identity, endpoints, accounts and credentials from those supplied files. For external authentication, explicitly supply the appropriate credential file, such as a service-account file where supported. Do not fall back to the machine's logged-in user, active project, environment variables or ambient credential discovery.
- Inject each consumer's required configuration and constructed collaborators. Behaviour code must not load configuration files, read environment variables or select credentials for itself.
- Pass file paths as arguments. Keep secret contents out of command arguments, source, console output and audit logs, following the project's established secret-storage policy.
- Fail startup with a precise error when required configuration is absent, invalid or inconsistent. Unit tests supply typed configuration directly in memory and test file loading separately at its boundary.
### Secrets

A credential is never committed, and never kept on one machine only. It lives in iCloud Drive, in one folder per project, so every Mac that checks the project out finds the same file at the same path: `"$HOME/Library/Mobile Documents/com~apple~CloudDocs/projects/<project>/"`.

- A checkout reaches its secret through a gitignored symlink under `secrets/`, made once per machine; code reads the link, never the iCloud path.
- Keep the link inside `secrets/`, never at the repo root: `docker build` stats every file in its context, and a secret at the root is one `.dockerignore` edit away from shipping inside an image.
- Quote the path. It contains a space, and the `~`s in `com~apple~CloudDocs` are literal: use `"$HOME/Library/Mobile Documents/…"`, not a bare `~/…`.
- iCloud can evict a file to a placeholder, so a read may block while macOS fetches it back, or fail offline. The copy also syncs to Apple, so a credential is retired by revoking it where it was issued; deleting the file does not.
- The TeamCity access key is the shared one, in `projects/teamcity/`.

*Generated from `npomfret/agent-standards`. Edit the standard there, not this copy.*
