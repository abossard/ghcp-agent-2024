---
applyTo: "**/*.java"
---

# Java Code Instructions

## Language Version
- Target Java 21 — use all modern features

## Style
- Use `var` for local variables when the type is clear
- Use records for immutable data carriers (DTOs, value objects)
- Use sealed interfaces where applicable
- Use pattern matching with `switch` and `instanceof`
- Use text blocks for multi-line strings
- Max line length: 120 characters
- Use 4 spaces for indentation

## Naming
- Classes: PascalCase (`GreetingService`)
- Methods: camelCase (`findByLanguage`)
- Constants: SCREAMING_SNAKE_CASE (`MAX_RETRIES`)
- Packages: lowercase (`com.example.demo.controller`)

## Imports
- No wildcard imports
- Order: java → jakarta → javax → org.springframework → com → others
- Remove unused imports

## Null Safety
- Never return null — use `Optional<T>`
- Use `@NonNull` / `@Nullable` annotations where helpful
- Use `Objects.requireNonNull()` in constructors for required parameters

## Logging
- Use SLF4J: `private static final Logger log = LoggerFactory.getLogger(ClassName.class);`
- DEBUG for flow tracing, INFO for important events, WARN for recoverable issues, ERROR for failures
- Never use `System.out.println` or `e.printStackTrace()`
