---
description: "Write comprehensive tests for an existing class"
agent: agent
tools:
  - codebase
  - terminal
  - editFiles
---

# Write Tests

Write comprehensive tests for the specified class or file.

## Process
1. Read the source file to understand its behavior
2. Identify all public methods and edge cases
3. Create appropriate test class:
   - **Service/Component**: Unit test with `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`
   - **Controller**: Integration test with `@SpringBootTest`, `@AutoConfigureMockMvc`, `MockMvc`
   - **Repository**: `@DataJpaTest` with test data

## Test Coverage Goals
- Happy path for each public method
- Error/edge cases (null input, empty collections, not found)
- Boundary conditions (empty string, max values)
- Exception scenarios

## Assertions
Use AssertJ:
```java
assertThat(result).isNotNull();
assertThat(result).hasSize(3);
assertThat(result.name()).isEqualTo("expected");
assertThat(result).isEmpty();
```

## Verify
Run `./mvnw test -Dtest={TestClassName}` after writing.
