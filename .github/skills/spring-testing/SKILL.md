---
name: spring-testing
description: >
  Spring Boot test automation skill. Covers unit testing, integration testing,
  MockMvc, Mockito, test slices, and test-driven development workflows.
  USE FOR: writing tests, test failures, test coverage, TDD, MockMvc, Mockito.
---

# Spring Testing Skill

## Procedure

### Running Tests
```bash
./mvnw test                                    # All tests
./mvnw test -Dtest=ClassName                   # Specific class
./mvnw test -Dtest=ClassName#methodName        # Specific method
./mvnw test -Dtest="com.example.demo.**"       # Package pattern
./mvnw verify                                  # Include integration tests
./mvnw test -Dmaven.test.failure.ignore=true   # Continue on failure
```

### Test Patterns

#### Unit Test (Service)
```java
@ExtendWith(MockitoExtension.class)
class ServiceTest {
    @Mock Repository repository;
    @InjectMocks Service service;

    @Test
    void shouldDoSomething() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        // Act
        var result = service.findById(1L);
        // Assert
        assertThat(result).isPresent();
        verify(repository).findById(1L);
    }
}
```

#### Integration Test (Controller)
```java
@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {
    @Autowired MockMvc mockMvc;

    @Test
    void shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/resource"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldCreateResource() throws Exception {
        var json = """
            {"name": "test", "value": "123"}
            """;
        mockMvc.perform(post("/api/resource")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated());
    }
}
```

#### Data Test (Repository)
```java
@DataJpaTest
class RepositoryTest {
    @Autowired TestEntityManager entityManager;
    @Autowired Repository repository;

    @Test
    void shouldFindByName() {
        entityManager.persist(new Entity("test"));
        var result = repository.findByName("test");
        assertThat(result).isNotEmpty();
    }
}
```

### Debugging Test Failures
1. Read the full stack trace
2. Check if it's a test setup issue (missing mock, wrong config)
3. Check if it's a code issue (logic error, null pointer)
4. Run with `-X` for debug output: `./mvnw test -X -Dtest=ClassName`
5. Add `@Disabled("reason")` temporarily to isolate failures
