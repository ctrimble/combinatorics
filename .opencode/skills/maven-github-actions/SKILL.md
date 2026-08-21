---
name: maven-github-actions
description: Use when wiring an open-source Maven project into GitHub Actions — building every branch with `mvn clean install`, activating a CI profile that adds verify-phase checks (formatting/verification), restricting artifact publishing to the canonical `develop` branch so forks never deploy, and documenting how to debug the pipeline.
---

# Maven + GitHub Actions CI/CD

Migrate a Maven project's CI/CD to GitHub Actions (typically replacing a legacy
`.travis.yml`, which is EOL). This skill covers the four goals most open-source
Java projects want: branch builds, a CI profile for verify-phase checks,
fork-safe publishing, and a troubleshooting reference.

## What goes where

- Workflow file: `.github/workflows/ci.yml` (one file is enough to start; split
  `ci.yml` / `release.yml` later if it grows).
- The `.github/workflows/` directory is discovered automatically. No repo setting
  is required to run, but **a fork's pushes do not reach the upstream repo** and
  **first-time PRs from forks need maintainer approval** (or `GITHUB_WORKFLOW`
  "Approve and run").
- **Secrets live at repo settings → Secrets and variables → Actions**, never in
  the workflow file. Fork PRs do not receive secrets (except a read-only
  `GITHUB_TOKEN`), which is the primary fork-safety mechanism.

## Goal 1 — Build every branch with `mvn clean install`

Trigger on pushes and pull requests on any branch, ignoring the site branch
(`gh-pages`) and any other non-build branches. Use `branches-ignore` rather than
an allow-list so new feature branches are covered automatically.

```yaml
on:
  push:
    branches-ignore:
      - gh-pages
  pull_request:
    branches-ignore:
      - gh-pages
  # Optional: allow manual runs and keep a release trigger.
  workflow_dispatch:
```

The build job. Gate on the JDK the project requires with a tool file so the
JDK version lives in version control:

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version-file: .java-version   # or: java-version: '17'
          cache: maven                       # caches ~/.m2 for this project

      - name: Build
        run: mvn -B -ntp clean install
```

`actions/setup-java` with `cache: maven` both installs the JDK and caches the
local Maven repository keyed off `**/pom.xml`, so repeated builds skip re-downloading
dependencies. Add `java-version-file: .java-version` (a single line: `17`, or a
`.sdkmanrc`/`.tool-versions` file) instead of a hard-coded `java-version` when
you want the JDK version tracked with the code. Use `-B` (batch mode, no
progress bars) and `-ntp` (no transfer progress) for clean, parseable logs.

## Goal 2 — A CI Maven profile that runs verify-phase checks

Create a dedicated profile (call it `ci`) that is **only** activated in CI. It
binds a formatting/verification goal into the `verify` phase (`verify` runs after
`test`, before `install`/`deploy`, so it gates the whole build). CI activates it
explicitly with `-Pci`; local and `mvn install` runs do not, so a developer's
`mvn install` is never blocked by a strict check.

In `pom.xml`:

```xml
<profiles>
  <profile>
    <id>ci</id>
    <!-- Not activeByDefault: only runs when -Pci is passed, i.e. in CI. -->
    <build>
      <plugins>
        <!-- Example: enforce formatting on the verify phase. Point at whatever
             formatter the project already uses (eclipse-codestyle, spotless,
             checkstyle, the project's own tidy/rewrite plugins, etc.). -->
        <plugin>
          <groupId>com.diffplug.spotless</groupId>
          <artifactId>spotless-maven-plugin</artifactId>
          <configuration>
            <failBuild>true</failBuild>   <!-- fail on unformatted code -->
          </configuration>
          <executions>
            <execution>
              <id>spotless-check</id>
              <phase>verify</phase>
              <goals><goal>check</goal></goals>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
  </profile>
</profiles>
```

Activate it from the workflow by passing the profile to Maven:

```yaml
      - name: Build
        run: mvn -B -ntp clean install -Pci
```

Project already has a `github` profile and a custom `maven-format-lifecycle`
extension that binds a `format` phase. Either (a) put the verify-phase check
inside the existing `github` profile and run `mvn ... -Pgithub`, or (b) add a
new `ci` profile as above. Prefer a dedicated `ci` profile: it keeps "run
formatting in CI" separate from the `github` profile's site-publishing
concerns, and the `-Pci` flag is self-documenting in every log.

Prefer a *check* goal (spotless `check`, checkstyle, `verify`-bound
tidy/rewrite) over a mutating *format* goal in CI. A check fails fast and
points the developer at the exact file; a mutating goal can silently rewrite
the tree and obscure the real diff.

## Goal 3 — Publish only from `develop` on the canonical repo

Two independent conditions must both hold to deploy, and they are checked with
job-level `if:` so the whole job is skipped (not just skipped mid-run):

1. **Event is a push** — `pull_request` runs are never publishing runs.
2. **Branch is `develop`** — `github.ref == 'refs/heads/develop'`.
3. **Repo is the canonical one, not a fork** — `github.repository == 'ctrimble/combinatorics'`.
   `github.repository`, `github.ref`, and `github.event_name` are all
   `pull_request`-safe expression contexts, so the same logic works in a PR
   context too.

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    # All three gates: a push, to develop, on the canonical repo only.
    if: github.event_name == 'push'
        && github.ref == 'refs/heads/develop'
        && github.repository == 'ctrimble/combinatorics'
    needs: build
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version-file: .java-version
          cache: maven
          # Wire credentials into Maven's settings.xml by env var name.
          server-id: sonatype-nexus-snapshots
          server-username-env-var: MAVEN_USERNAME
          server-password-env-var: MAVEN_PASSWORD
          gpg-private-key: ${{ secrets.GPG_PRIVATE_KEY }}
          gpg-passphrase-env-var: GPG_PASSPHRASE

      - name: Deploy
        run: mvn -B deploy -Dmaven.test.skip=true -Dgpg.passphrase=${{ secrets.GPG_PASSPHRASE }}
        env:
          MAVEN_USERNAME: ${{ secrets.MAVEN_USERNAME }}
          MAVEN_PASSWORD: ${{ secrets.MAVEN_PASSWORD }}
          GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
```

Why this is fork-safe:

- Forks have **no push events on the upstream repo**, so a fork's `develop`
  branch never satisfies `github.event_name == 'push'` on the canonical repo.
  Deploy simply never runs for a fork push.
- Even for PRs from forks, `github.repository` is the **base** repo, but secrets
  (`MAVEN_USERNAME`, `MAVEN_PASSWORD`, `GPG_*`) are **not** injected into
  fork-sourced runs — a deploy would fail at the credential step. The
  `github.repository == 'ctrimble/combinatorics'` check makes the intent
  explicit and guards against `pull_request_target`, where the base repo context
  is mixed with untrusted head-repo code.
- Defense in depth: require **branch protection** on `develop` with required
  status checks, and store all publish secrets at the repo level.

Credential wiring options:

- `setup-java`'s `server-id` / `server-username-env-var` /
  `server-password-env-var` inputs generate a Maven `settings.xml` for you —
  the recommended path. The `<server>` `id` must match the `<server id>` in the
  project's `settings.xml`/distributionManagement (here
  `sonatype-nexus-snapshots`).
- For GPG signing, `gpg-private-key` + `gpg-passphrase-env-var` import an
  isolated keyring; the `maven-gpg-plugin` must be 3.2.0+ for the
  `gpg.passphraseEnvName` path.
- This project currently uses the legacy `maven-deploy-plugin`. To publish to
  Maven Central properly, migrate to
  `org.sonatype.central:central-publishing-maven-plugin` with a
  `server.id = central-portal` and a `central.sonatype.com` token, and gate
  release builds on tag push (`on.push.tags: ['v*']`) instead of every
  `develop` push when you graduate to releasing.

## Goal 4 — Document the pipeline and how to debug it

Add a `BUILD.md` (or a "CI" section in `CONTRIBUTING.md`) so a future
maintainer can tell what to inspect when a run breaks. Suggested content:

- **What runs, when.** Push to any branch except `gh-pages` → `build` runs
  `mvn clean install -Pci`. A push to `develop` on the canonical repo →
  `deploy` runs.
- **JDK & Maven.** JDK comes from `.java-version` via `actions/setup-java`;
  Maven is preinstalled on the runner (no wrapper needed, but pin with
  `mvnw` if you add one). The build requires Maven ≥ the version in the
  project's enforcer rule.
- **Profile.** `-Pci` turns on verify-phase checks (formatting, etc.). Local
  `mvn install` skips them; CI never runs without `-Pci`.
- **Publishing.** Deploy is gated on `push` + `develop` + canonical repo.
  Secrets required: `MAVEN_USERNAME`, `MAVEN_PASSWORD`, `GPG_PRIVATE_KEY`,
  `GPG_PASSPHRASE` (and `central token` if using central-publishing). Missing
  secret ⇒ deploy errors at the credential step, which is desired for forks.
- **If a build breaks, check in this order:**
  1. JDK version — is `.java-version` still installable on `ubuntu-latest`?
  2. Maven version — did the enforcer's minimum bump past what the runner ships?
  3. Cache — a stale `~/.m2` cache rarely breaks, but `actions/setup-java`
     re-keys it on `pom.xml` hash; clear via Actions → the run's cache.
  4. Profile — did a verify-phase check (formatting) start failing? Re-run the
     same goal locally with the same flags:
     `mvn -B -ntp clean install -Pci`.
  5. Deploy — confirm `github.repository`, `github.ref`, and that the four
     secrets exist at **repo** settings (a fork's run legitimately lacks them).
- **Permissions.** Keep the `build` job's `permissions: { contents: read }`.
  Only the `deploy` job needs write; scope it to `pull-requests: write`,
  `contents: write` as needed.
- **Approving fork PRs.** First-time contributor PRs need "Approve and run"
  from a maintainer; the build will sit pending until then.

## Putting it together (complete `ci.yml`)

```yaml
name: CI
on:
  push:
    branches-ignore: [gh-pages]
  pull_request:
    branches-ignore: [gh-pages]
  workflow_dispatch:

permissions:
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version-file: .java-version
          cache: maven
      - name: Build
        run: mvn -B -ntp clean install -Pci

  deploy:
    runs-on: ubuntu-latest
    needs: build
    if: github.event_name == 'push'
        && github.ref == 'refs/heads/develop'
        && github.repository == 'ctrimble/combinatorics'
    permissions:
      contents: write
      pull-requests: write
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version-file: .java-version
          cache: maven
          server-id: sonatype-nexus-snapshots
          server-username-env-var: MAVEN_USERNAME
          server-password-env-var: MAVEN_PASSWORD
          gpg-private-key: ${{ secrets.GPG_PRIVATE_KEY }}
          gpg-passphrase-env-var: GPG_PASSPHRASE
      - name: Deploy
        run: mvn -B deploy -Dmaven.test.skip=true -Dgpg.passphrase=${{ secrets.GPG_PASSPHRASE }}
        env:
          MAVEN_USERNAME: ${{ secrets.MAVEN_USERNAME }}
          MAVEN_PASSWORD: ${{ secrets.MAVEN_PASSWORD }}
          GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
```

## Verification before merging the workflow

- Push a commit to a scratch branch and confirm the **Actions** tab shows a
  `build` run that executes `mvn clean install -Pci`.
- Confirm a push to `develop` on the **canonical** repo triggers **deploy**,
  while a push to `develop` on a **fork** triggers only `build` (deploy is
  skipped). Check the run log for the `if:` gate skipping the job.
- Confirm the `gh-pages` branch does **not** trigger a build.
- Run the exact CI command locally with the profile — `mvn -B -ntp clean
  install -Pci` — to keep the developer and CI views in sync.
- Replace `.travis.yml` and remove the `<ciManagement>` block in `pom.xml`
  once the Actions pipeline is green.
