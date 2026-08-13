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

## Branch Guidelines
- When working on the `opencode-develop` branch, make logical commits for each group of changes
- This ensures proper version control and easier code review

## Important Notes
- This is a multi-module Maven project with core library and CLI modules
- The CLI tool is built as an executable jar using maven-shade-plugin
- Core library uses Apache Commons Math and Javolution dependencies
- Tests use JUnit 4, Mockito, and JUnit-Benchmarks
- The project uses GitFlow branching strategy (as indicated in pom.xml)