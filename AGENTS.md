# Combinatorics Repository Agent Guide

## Project Structure
- **Parent POM**: `/pom.xml` (controls build, dependencies, and plugins)
- **Core Library**: `/combinatorics/` (main library code)
- **CLI Tool**: `/combinatorics-cli/` (command-line interface)
- **Build System**: Maven 3.0+ with Java 17+

## Key Commands
- Build project: `mvn clean install`
- Run tests: `mvn test`
- Generate site documentation: `mvn site`
- Format POM and Java sources: `mvn format`

## Formatting
- After editing `pom.xml` files or Java sources, run `mvn format` to apply the
project's formatting. `mvn format` runs the `format` lifecycle phase, which invokes
the `tidy-maven-plugin` (POM structure) and the `rewrite-maven-plugin`
(Javadoc/comment alignment) to normalize formatting.
- Do not hand-format POM or Java files; add valid content and let `mvn format`
produce the correct formatting, then run `mvn format` before committing.
- CI runs `mvn clean install -Pci`, which binds the `tidy-maven-plugin:check`
goal to the `verify` phase and fails the build if a POM is not formatted. Run
`mvn format` (or `mvn tidy:pom` for POMs only) locally to stay in sync.

## Branch Guidelines
- When working on the `opencode-develop` branch, make logical commits for each group of changes
- This ensures proper version control and easier code review

## Important Notes
- This is a multi-module Maven project with core library and CLI modules
- The CLI tool is built as an executable jar using maven-shade-plugin
- Core library uses Apache Commons Math and Javolution dependencies
- Tests use JUnit 4, Mockito, and JUnit-Benchmarks
- The project uses GitFlow branching strategy (as indicated in pom.xml)