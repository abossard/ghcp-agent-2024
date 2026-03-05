# AGENTS.md — Agentic Development Guide for Spring Boot (FSI Environment)

> This file instructs AI coding agents on how to work with this Spring Boot project.
> It is optimized for GitHub Copilot Agent Mode in a restricted FSI environment where MCP is blocked.

## Environment Constraints

- **No MCP**: Model Context Protocol is blocked by organizational policy. All agent capabilities must use built-in VS Code tools, terminal commands, and file operations only.
- **No Open Code**: Only the official VS Code + GitHub Copilot is available. No third-party AI tools.
- **Limited Premium Tokens**: Be efficient with token usage. Prefer concise, targeted operations.
- **WSL2 Incompatibility**: Windows Copilot extensions may not work in WSL2. Prefer native Windows or CLI-based workflows.

## Tech Stack

| Component       | Technology                          |
|-----------------|-------------------------------------|
| Language        | Java 17+ (LTS, Java 21 recommended)    |
| Framework       | Spring Boot 3.4.x                   |
| Build Tool      | Maven 3.9+ (use `./mvnw`)           |
| Database        | H2 (dev), PostgreSQL (prod)         |
| Testing         | JUnit 5, Mockito, MockMvc           |
| API Style       | REST (JSON)                          |
| Java Features   | Records (17+), Virtual Threads (21+), Pattern Matching (17+) |

## Project Structure

```
src/main/java/com/example/demo/
├── DemoApplication.java          # Spring Boot entry point
├── controller/                   # REST controllers (@RestController)
├── service/                      # Business logic (@Service)
├── model/                        # JPA entities and records (@Entity)
├── repository/                   # Spring Data JPA repositories
└── config/                       # Configuration classes

src/test/java/com/example/demo/
├── controller/                   # Integration tests with MockMvc
└── service/                      # Unit tests with Mockito

src/main/resources/
├── application.yml               # Main configuration
└── application-{profile}.yml     # Profile-specific config
```

## Commands — Use These (CLI-Focused)

All commands use the Maven Wrapper (`./mvnw`) to ensure consistent builds:

```bash
# Build
./mvnw clean compile                    # Compile the project
./mvnw clean package -DskipTests        # Package without tests
./mvnw clean package                    # Full build with tests

# Test
./mvnw test                             # Run all tests
./mvnw test -Dtest=ClassName            # Run specific test class
./mvnw test -Dtest=ClassName#methodName # Run specific test method
./mvnw verify                           # Run integration tests

# Run
./mvnw spring-boot:run                  # Start the application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev  # With profile

# Dependencies
./mvnw dependency:tree                  # Show dependency tree
./mvnw versions:display-dependency-updates  # Check for updates

# Code Quality
./mvnw checkstyle:check                 # Run checkstyle (if configured)
./mvnw spotbugs:check                   # Run SpotBugs (if configured)
```

## Coding Conventions

### Java Style
- Use Java 21 records for DTOs and value objects
- Use constructor injection (never field injection with `@Autowired`)
- Use `Optional` for nullable return values — never return null
- Use `var` for local variables when the type is obvious
- Use text blocks (`"""`) for multi-line strings
- Use pattern matching with `switch` and `instanceof`

### REST API Design
- Base path: `/api/{resource}` (plural nouns)
- Use proper HTTP methods: GET (read), POST (create), PUT (update), DELETE (remove)
- Return proper status codes: 200 OK, 201 Created, 204 No Content, 404 Not Found, 400 Bad Request
- Use `@Valid` for request body validation
- Return `ResponseEntity<T>` for explicit status control

### Testing
- Every controller must have integration tests using `@SpringBootTest` + `@AutoConfigureMockMvc`
- Every service must have unit tests using `@ExtendWith(MockitoExtension.class)`
- Use AssertJ for assertions (`assertThat()`)
- Follow the AAA pattern: Arrange, Act, Assert
- Test method naming: `should{ExpectedBehavior}When{Condition}`

### Error Handling
- Use `@ControllerAdvice` for global exception handling
- Return structured error responses with timestamp, status, message, path
- Never expose stack traces in API responses
- Log exceptions at appropriate levels (ERROR for unexpected, WARN for business rule violations)

### Security (FSI Requirements)
- Never hardcode credentials, tokens, or secrets
- Use environment variables or Spring Config Server for secrets
- Validate ALL user input
- Use parameterized queries (JPA handles this)
- Add appropriate CORS configuration for frontend integration
- Follow OWASP Top 10 guidelines

## Output Examples

### Controller Example
```java
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable Long id) {
        return itemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item create(@Valid @RequestBody CreateItemRequest request) {
        return itemService.create(request);
    }
}
```

### Service Example
```java
@Service
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public Optional<Item> findById(Long id) {
        return repository.findById(id);
    }

    public Item create(CreateItemRequest request) {
        var item = new Item(request.name(), request.description());
        return repository.save(item);
    }
}
```

### Test Example
```java
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;

    @InjectMocks
    private ItemService service;

    @Test
    void shouldReturnItemWhenExists() {
        var item = new Item(1L, "Test", "Description");
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        var result = service.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Test");
    }
}
```

## Boundaries — DO NOT

- **NEVER** modify `pom.xml` parent version without explicit approval
- **NEVER** add dependencies without justification
- **NEVER** commit secrets, passwords, or API keys
- **NEVER** use field injection (`@Autowired` on fields)
- **NEVER** return null from service methods — use `Optional`
- **NEVER** ignore test failures — fix them before proceeding
- **NEVER** use `System.out.println` — use SLF4J logging
- **NEVER** suppress exceptions silently
- **NEVER** use `@SuppressWarnings` without a comment explaining why
- **NEVER** create MCP configurations (blocked by policy)

## Workflow

1. **Understand** the task fully before making changes
2. **Check** existing tests pass: `./mvnw test`
3. **Implement** the change following conventions above
4. **Write tests** for all new code
5. **Verify** all tests pass: `./mvnw test`
6. **Review** the diff: `git diff`
7. **Commit** with a clear message following conventional commits
