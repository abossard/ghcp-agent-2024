---
description: "Write comprehensive tests for an existing class"
---

# Write Tests

Write comprehensive tests for the specified class or file.
Follow testing.instructions.md and the spring-testing skill for conventions.

1. Read the source file, identify all public methods and edge cases
2. Create test class (see existing tests for pattern)
3. Cover: happy path, errors, edge cases, boundaries
4. Run `./mvnw test -Dtest={TestClassName}` to verify
