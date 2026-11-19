#!/usr/bin/env python3
"""Hold shell scripts to the scripting standard: shfmt, strict mode and shellcheck.

  lint_shell.py --hook       PostToolUse: read the event on stdin, check the edited file
  lint_shell.py FILE...      check these files
  lint_shell.py --selftest   run known good and bad scripts through the checks

A file is a shell script when it ends in .sh or .bash, or has no suffix and a
shebang naming bash or sh. It is formatted in place with shfmt first, using the
project's .editorconfig when it has one and `-i 2 -ci` when it does not, then
checked: the shebang is `#!/usr/bin/env bash`, `set -euo pipefail` comes before
the first command, and shellcheck reports no substantive findings. SC2004 and
SC2006 describe equivalent arithmetic/command-substitution spellings; formatting
and those cosmetic preferences never reject an otherwise valid script.

Exit 0 when clean or not a shell script, 1 when a named file fails, 2 from the
hook so Claude Code shows the findings to the agent, and 2 for bad usage.

shellcheck and shfmt are not optional. When either is missing the hook says so,
once per session, and names the brew command for the user to run, rather than
passing scripts it never looked at.
"""
import hashlib
import json
import pathlib
import re
import shutil
import subprocess
import sys
import tempfile

SUFFIXES = {".sh", ".bash"}
SHEBANG = "#!/usr/bin/env bash"
STRICT = re.compile(r"^set\s+-[a-zA-Z]*e[a-zA-Z]*u[a-zA-Z]*o\s+pipefail\b")
SKIPPED = {"node_modules", ".git", "dist", "build", "Pods", "DerivedData"}
TOOLS = ("shellcheck", "shfmt")
INSTALL = "brew install shellcheck shfmt"
# Measured with equivalent arithmetic and backtick examples during the local
# rollout. Do not filter by severity: SC2086 is informational but catches unsafe
# word splitting. Keep syntax, strict-mode and every other defect check.
COSMETIC_CODES = ("SC2004", "SC2006")
TOOL_TIMEOUT_SECONDS = 10


def is_shell(path):
    if SKIPPED.intersection(path.parts) or not path.is_file():
        return False
    if path.suffix in SUFFIXES:
        return True
    if path.suffix:
        return False
    try:
        first = path.open(errors="replace").readline()
    except OSError:
        return False
    return bool(re.match(r"#!\s*/(usr/)?bin/(env\s+)?(ba)?sh\b", first))


def has_editorconfig(path):
    for folder in path.resolve().parents:
        if (folder / ".editorconfig").is_file():
            return True
        if (folder / ".git").exists():
            return False
    return False


def missing_tools():
    return [tool for tool in TOOLS if shutil.which(tool) is None]


def header_findings(path):
    lines = path.read_text(errors="replace").splitlines()
    findings = []
    if not lines or lines[0].strip() != SHEBANG:
        findings.append(f"{path}:1: the first line must be `{SHEBANG}`")
    for number, line in enumerate(lines[1:], start=2):
        text = line.strip()
        if not text or text.startswith("#"):
            continue
        if not STRICT.match(text):
            findings.append(
                f"{path}:{number}: `set -euo pipefail` must come before the first command"
            )
        break
    else:
        findings.append(f"{path}: has no `set -euo pipefail`")
    return findings


def check(path):
    """Format the file in place, then return its findings as lines of text."""
    style = [] if has_editorconfig(path) else ["-i", "2", "-ci"]
    findings = []
    try:
        formatted = subprocess.run(
            ["shfmt", *style, "-w", str(path)], capture_output=True, text=True,
            timeout=TOOL_TIMEOUT_SECONDS,
        )
    except subprocess.TimeoutExpired:
        return [f"{path}: shfmt exceeded {TOOL_TIMEOUT_SECONDS}s while formatting"]
    if formatted.returncode != 0:
        findings.append(f"{path}: shfmt could not parse it: {formatted.stderr.strip()}")
    findings += header_findings(path)
    try:
        linted = subprocess.run(
            ["shellcheck", "--external-sources", "--exclude=" + ",".join(COSMETIC_CODES),
             "--format=gcc", str(path)],
            capture_output=True, text=True, cwd=path.parent,
            timeout=TOOL_TIMEOUT_SECONDS,
        )
    except subprocess.TimeoutExpired:
        return findings + [f"{path}: shellcheck exceeded {TOOL_TIMEOUT_SECONDS}s while checking"]
    findings += [line for line in linted.stdout.splitlines() if line.strip()]
    if linted.returncode and not linted.stdout.strip():
        findings.append(f"{path}: shellcheck failed ({linted.returncode}): {linted.stderr.strip()}")
    return findings


def warned(session, root):
    # The hook protocol supplies the session identity; treat it as an opaque key.
    key = hashlib.sha256((session or "unknown").encode()).hexdigest()
    marker = root / "tmp" / "scripting-lint" / key
    if marker.exists():
        return True
    marker.parent.mkdir(parents=True, exist_ok=True)
    marker.touch()
    return False


def hook():
    try:
        event = json.load(sys.stdin)
    except ValueError:
        return 0
    name = (event.get("tool_input") or {}).get("file_path")
    if not name:
        return 0
    path = pathlib.Path(name)
    if not path.is_absolute():
        path = pathlib.Path(event.get("cwd") or ".") / path
    if not is_shell(path):
        return 0
    missing = missing_tools()
    if missing:
        if warned(event.get("session_id"), pathlib.Path(event.get("cwd") or ".")):
            return 0
        print(
            f"{path} was not checked: {' and '.join(missing)} not installed. "
            f"The scripting standard requires both. Ask the user to run `! {INSTALL}`, "
            f"then edit the script again.",
            file=sys.stderr,
        )
        return 2
    findings = check(path)
    if not findings:
        return 0
    print("The scripting standard rejects this script. Fix every finding:", file=sys.stderr)
    print("\n".join(findings), file=sys.stderr)
    return 2


def files(names):
    missing = missing_tools()
    if missing:
        print(f"{' and '.join(missing)} not installed: run `{INSTALL}`", file=sys.stderr)
        return 1
    failed = False
    for name in names:
        path = pathlib.Path(name)
        if not is_shell(path):
            print(f"{path}: not a shell script, skipped", file=sys.stderr)
            continue
        findings = check(path)
        if findings:
            failed = True
            print("\n".join(findings), file=sys.stderr)
    return 1 if failed else 0


CASES = (
    ("clean.sh", f'{SHEBANG}\nset -euo pipefail\n\nmain() {{\n  echo "$1"\n}}\n\nmain "$@"\n', None),
    ("comment-first.sh", f'{SHEBANG}\n# Says hello.\nset -euo pipefail\necho "hi"\n', None),
    ("no-suffix", f'{SHEBANG}\nset -euo pipefail\necho "hi"\n', None),
    ("sh-shebang.sh", '#!/bin/sh\nset -euo pipefail\necho "hi"\n', "first line must be"),
    ("no-strict.sh", f'{SHEBANG}\necho "hi"\n', "must come before the first command"),
    ("late-strict.sh", f'{SHEBANG}\ncd /tmp\nset -euo pipefail\n', "must come before the first command"),
    ("empty-body.sh", f"{SHEBANG}\n", "has no `set -euo pipefail`"),
    ("unquoted.sh", f'{SHEBANG}\nset -euo pipefail\nrm $1\n', "SC2086"),
    ("bad-syntax.sh", f'{SHEBANG}\nset -euo pipefail\nif then\n', "shfmt could not parse"),
    ("arithmetic-style.sh", f'{SHEBANG}\nset -euo pipefail\ncount=2\necho "$(( $count + 1 ))"\n', None),
    ("substitution-style.sh", f'{SHEBANG}\nset -euo pipefail\nvalue=`date`\nprintf "%s\\n" "$value"\n', None),
)


def selftest():
    missing = missing_tools()
    if missing:
        print(f"{' and '.join(missing)} not installed: run `{INSTALL}`", file=sys.stderr)
        return 1
    failures = 0
    scratch = pathlib.Path.cwd() / "tmp"
    scratch.mkdir(exist_ok=True)
    with tempfile.TemporaryDirectory(dir=scratch) as folder:
        # Fixtures test a fixed style, independently of the caller's project.
        (pathlib.Path(folder) / ".editorconfig").write_text(
            "root = true\n[*]\nindent_style = space\nindent_size = 2\n"
        )
        for name, text, expected in CASES:
            path = pathlib.Path(folder) / name
            path.write_text(text)
            findings = "\n".join(check(path))
            ok = expected in findings if expected else findings == ""
            if not ok:
                failures += 1
                print(f"FAIL {name}: expected {expected or 'no findings'}, got:\n{findings}", file=sys.stderr)
        formatted = pathlib.Path(folder) / "format.sh"
        formatted.write_text(f'{SHEBANG}\nset -euo pipefail\nif true; then\necho "x"\nfi\n')
        check(formatted)
        if '  echo "x"' not in formatted.read_text():
            failures += 1
            print("FAIL format.sh: shfmt did not indent the body", file=sys.stderr)
        if is_shell(pathlib.Path(folder) / "missing.sh"):
            failures += 1
            print("FAIL missing.sh: a file that does not exist counted as a script", file=sys.stderr)
    total = len(CASES) + 2
    print(f"selftest: {total - failures}/{total} passed")
    return 1 if failures else 0


def main():
    args = sys.argv[1:]
    if args == ["--hook"]:
        return hook()
    if args == ["--selftest"]:
        return selftest()
    if args == ["--help"]:
        print(__doc__)
        return 0
    if not args or any(a.startswith("-") for a in args):
        print(__doc__, file=sys.stderr)
        return 2
    return files(args)


if __name__ == "__main__":
    sys.exit(main())
