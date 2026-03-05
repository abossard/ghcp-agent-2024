---
description: "Refactor a service class for better design, testability, and maintainability"
---

# Refactor Service

Refactor the specified service class to improve design, testability, and maintainability.

## What to Look For

1. **Single Responsibility**: Does the service do too much? Split if needed.
2. **Constructor Injection**: Convert any field injection to constructor injection.
3. **Interface Extraction**: Consider extracting an interface if the service is used across layers.
4. **Return Types**: Replace nullable returns with `Optional<T>`.
5. **Java 21 Features**: Use records, pattern matching, text blocks where beneficial.
6. **Error Handling**: Use custom exceptions instead of generic ones.
7. **Logging**: Add appropriate SLF4J logging at DEBUG/INFO/WARN/ERROR levels.

## After Refactoring

1. Update all existing tests to match new signatures
2. Add new tests for any new behavior
3. Run `./mvnw test` to verify nothing is broken
4. Run `git diff` to review all changes
