# Project-Wide Copilot Instructions

You are working on the **spring-petclinic-rest** application — a Spring Boot 3.4.x REST API using Java 21, based on `org.springframework.samples.petclinic`.

## Critical Constraints
- **NO MCP**: Never suggest or configure MCP servers. They are blocked by organizational policy.
- **CLI-First**: Always prefer command-line tools over GUI operations. Use `./mvnw` for all Maven operations.
- **Token Efficiency**: Be concise. Minimize unnecessary output. Get to the point.

## Code Generation Rules
- Use Java 21 features: records, pattern matching, virtual threads, text blocks, sealed interfaces
- Use constructor injection exclusively (never `@Autowired` on fields)
- Return `Optional<T>` instead of nullable values
- Use `var` for local variables when type is obvious from the right-hand side
- Use SLF4J for logging (`private static final Logger log = LoggerFactory.getLogger(ClassName.class)`)
- Annotate `@Valid` on request bodies; use Bean Validation annotations on fields
- Package: `org.springframework.samples.petclinic`
- Entity classes in `model/` (extend BaseEntity, NamedEntity, or Person)
- REST controllers in `rest/controller/` (NOT `controller/`)
- Business logic in `ClinicService` interface + `ClinicServiceImpl` (NOT per-entity services)
- **OpenAPI-first**: DTOs are generated from `src/main/resources/openapi.yml` — never write DTOs manually
- **MapStruct mappers** in `mapper/` for entity ↔ DTO conversion
- Configuration uses `application.properties` (not .yml), port 9966, context path `/petclinic/`
- Spring Security is included but disabled by default

## Testing Requirements
- Write tests for every new class
- Controllers: `@SpringBootTest` + `@AutoConfigureMockMvc` with MockMvc
- Services: `@ExtendWith(MockitoExtension.class)` with `@Mock` and `@InjectMocks`
- Use AssertJ assertions
- Method naming: `should{Expected}When{Condition}`

## Security (FSI)
- Never hardcode secrets or credentials
- Always validate user input
- Never expose stack traces in API responses
- Use parameterized queries (JPA provides this)
- Follow OWASP Top 10 guidelines

## Before Finishing Any Task
1. Run `./mvnw test` to verify all tests pass
2. Run `git diff` to review changes
3. Ensure no secrets are in the diff
