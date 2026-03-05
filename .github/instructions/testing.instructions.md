---
applyTo: "src/test/**/*.java"
---

# Testing Instructions

## Framework Stack
- JUnit 5 (Jupiter) for test execution
- Mockito for mocking
- AssertJ for fluent assertions
- MockMvc for controller integration tests
- Spring Boot Test for full context tests

## Test Types

### Unit Tests (Service layer)
```java
@ExtendWith(MockitoExtension.class)
class MyServiceTest {
    @Mock private MyRepository repository;
    @InjectMocks private MyService service;

    @Test
    void shouldReturnItemWhenExists() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(item));
        // Act
        var result = service.findById(1L);
        // Assert
        assertThat(result).isPresent();
    }
}
```

### Integration Tests (Controller layer)
```java
@SpringBootTest
@AutoConfigureMockMvc
class MyControllerTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/items"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
```

## Naming Convention
- Test class: `{ClassUnderTest}Test`
- Test method: `should{ExpectedBehavior}When{Condition}`
- Examples: `shouldReturnEmptyListWhenNoItems`, `shouldReturn404WhenNotFound`

## AAA Pattern
Every test MUST follow Arrange-Act-Assert:
1. **Arrange**: Set up test data and mocks
2. **Act**: Call the method under test
3. **Assert**: Verify the result

## CLI Commands
```bash
./mvnw test                              # Run all tests
./mvnw test -Dtest=ClassName             # Run specific class
./mvnw test -Dtest=ClassName#methodName  # Run specific method
./mvnw test -pl module-name              # Run tests in module
./mvnw verify                            # Run integration tests
```
