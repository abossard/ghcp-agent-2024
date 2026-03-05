---
name: spring-testing
description: >
  Spring Boot test automation skill. Covers unit testing, integration testing,
  MockMvc, Mockito, test slices, and test-driven development workflows.
  USE FOR: writing tests, test failures, test coverage, TDD, MockMvc, Mockito.
---

# Spring Testing Skill

See testing.instructions.md for patterns (unit, integration, data tests).

## Commands

```bash
./mvnw test                                    # All tests
./mvnw test -Dtest=ClassName                   # Specific class
./mvnw test -Dtest=ClassName#methodName        # Specific method
./mvnw test -Dtest="com.example.demo.**"       # Package pattern
./mvnw verify                                  # Include integration tests
```

## Debugging Test Failures

1. Read the full stack trace
2. Check setup issues (missing mock, wrong config) vs code issues
3. Run with `-X` for debug output: `./mvnw test -X -Dtest=ClassName`
4. Add `@Disabled("reason")` temporarily to isolate failures
