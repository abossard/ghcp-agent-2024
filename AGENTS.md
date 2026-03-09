# AGENTS.md — Agentic Development Guide for Spring PetClinic REST (FSI Environment)

> This file instructs AI coding agents on how to work with the Spring PetClinic REST API project.
> It is optimized for GitHub Copilot Agent Mode in a restricted FSI environment where MCP is blocked.

## Environment Constraints

- **No MCP**: Model Context Protocol is blocked by organizational policy. All agent capabilities must use built-in VS Code tools, terminal commands, and file operations only.
- **No Open Code**: Only the official VS Code + GitHub Copilot is available. No third-party AI tools.
- **Limited Premium Tokens**: Be efficient with token usage. Prefer concise, targeted operations.
- **WSL2 Incompatibility**: Windows Copilot extensions may not work in WSL2. Prefer native Windows or CLI-based workflows.

## Tech Stack

| Component          | Technology                                                    |
|--------------------|---------------------------------------------------------------|
| Language           | Java 17 (LTS)                                                |
| Framework          | Spring Boot 3.4.3                                            |
| Build Tool         | Maven 3.9+ (use `./mvnw`)                                   |
| Database           | H2 (default), HSQLDB, MySQL, PostgreSQL (via Spring profiles)|
| Testing            | JUnit 5, Mockito, MockMvc, Spring Security Test              |
| API Style          | REST (JSON), OpenAPI-first code generation                   |
| API Docs           | springdoc-openapi (Swagger UI)                               |
| DTO Generation     | openapi-generator-maven-plugin (from `openapi.yml`)          |
| Object Mapping     | MapStruct (entity <-> DTO)                                   |
| Security           | Spring Security (basic auth, disabled by default)            |
| Persistence        | JDBC, JPA, or Spring Data JPA (selected via Spring profiles) |

## Project Structure

```
src/main/java/org/springframework/samples/petclinic/
├── PetClinicApplication.java              # Spring Boot entry point
├── config/
│   └── SwaggerConfig.java                 # OpenAPI/Swagger configuration
├── mapper/                                # MapStruct mappers (entity <-> DTO)
│   ├── OwnerMapper.java
│   ├── PetMapper.java
│   ├── PetTypeMapper.java
│   ├── SpecialtyMapper.java
│   ├── UserMapper.java
│   ├── VetMapper.java
│   └── VisitMapper.java
├── model/                                 # JPA entities (@Entity)
│   ├── BaseEntity.java                    # Base: id, isNew()
│   ├── NamedEntity.java                   # Extends BaseEntity: adds name
│   ├── Person.java                        # Extends BaseEntity: firstName, lastName
│   ├── Owner.java                         # Extends Person: address, city, telephone, pets
│   ├── Pet.java                           # Extends NamedEntity: birthDate, type, owner, visits
│   ├── PetType.java                       # Extends NamedEntity
│   ├── Vet.java                           # Extends Person: specialties
│   ├── Specialty.java                     # Extends NamedEntity
│   ├── Visit.java                         # Extends BaseEntity: date, description, pet
│   ├── User.java                          # username, password, enabled, roles
│   └── Role.java                          # Extends BaseEntity: name, user
├── repository/                            # Repository interfaces
│   ├── OwnerRepository.java
│   ├── PetRepository.java
│   ├── PetTypeRepository.java
│   ├── SpecialtyRepository.java
│   ├── VetRepository.java
│   ├── VisitRepository.java
│   └── UserRepository.java
│   ├── jdbc/                              # JDBC implementations (profile: jdbc)
│   ├── jpa/                               # JPA implementations (profile: jpa)
│   └── springdatajpa/                     # Spring Data JPA implementations (profile: spring-data-jpa)
├── rest/
│   ├── advice/
│   │   └── ExceptionControllerAdvice.java # Global exception handling
│   ├── controller/                        # REST controllers (@RestController)
│   │   ├── OwnerRestController.java       # Implements generated OwnersApi
│   │   ├── PetRestController.java         # Implements generated PetsApi
│   │   ├── PetTypeRestController.java     # Implements generated PettypesApi
│   │   ├── VetRestController.java         # Implements generated VetsApi
│   │   ├── VisitRestController.java       # Implements generated VisitsApi
│   │   ├── SpecialtyRestController.java   # Implements generated SpecialtiesApi
│   │   ├── UserRestController.java        # Implements generated UsersApi
│   │   ├── RootRestController.java        # Root redirect
│   │   └── BindingErrorsResponse.java     # Validation error helper
│   ├── api/                               # (GENERATED) API interfaces from openapi.yml
│   └── dto/                               # (GENERATED) DTO classes from openapi.yml
├── security/
│   ├── BasicAuthenticationConfig.java     # Enabled when petclinic.security.enable=true
│   ├── DisableSecurityConfig.java         # Active when petclinic.security.enable=false
│   └── Roles.java                         # Role constants (OWNER_ADMIN, VET_ADMIN, ADMIN)
├── service/                               # Business logic (@Service)
│   ├── ClinicService.java                 # Facade interface for all domain operations
│   ├── ClinicServiceImpl.java             # Implementation delegating to repositories
│   ├── UserService.java                   # User management interface
│   └── UserServiceImpl.java              # User management implementation
└── util/                                  # Utility classes

src/main/resources/
├── application.properties                 # Main config (port 9966, context /petclinic/)
├── application-h2.properties              # H2 database config (default)
├── application-hsqldb.properties          # HSQLDB config
├── application-mysql.properties           # MySQL config
├── application-postgres.properties        # PostgreSQL config
├── openapi.yml                            # OpenAPI 3.0 spec (source of truth for DTOs & API interfaces)
├── db/                                    # SQL schema & data scripts per database
└── logback.xml                            # Logging configuration

target/generated-sources/openapi/src/main/java/org/springframework/samples/petclinic/rest/
├── api/                                   # Generated API interfaces (OwnersApi, PetsApi, etc.)
└── dto/                                   # Generated DTOs (OwnerDto, OwnerFieldsDto, PetDto, etc.)

src/test/java/org/springframework/samples/petclinic/
├── rest/controller/                       # Controller tests with MockMvc + @WithMockUser
│   ├── OwnerRestControllerTests.java
│   ├── PetRestControllerTests.java
│   ├── VetRestControllerTests.java
│   └── ...
├── service/
│   ├── clinicService/                     # ClinicService integration tests per persistence profile
│   │   ├── AbstractClinicServiceTests.java
│   │   ├── ClinicServiceH2JdbcTests.java
│   │   ├── ClinicServiceHsqlJdbcTests.java
│   │   ├── ClinicServiceJpaTests.java
│   │   └── ClinicServiceSpringDataJpaTests.java
│   └── userService/                       # UserService integration tests per persistence profile
└── model/
    └── ValidatorTests.java
```

## Commands — Use These (CLI-Focused)

All commands use the Maven Wrapper (`./mvnw`) to ensure consistent builds:

```bash
# Build (also generates DTOs and API interfaces from openapi.yml)
./mvnw clean compile                    # Compile + generate OpenAPI code
./mvnw clean package -DskipTests        # Package without tests
./mvnw clean package                    # Full build with tests

# Test
./mvnw test                             # Run all tests
./mvnw test -Dtest=ClassName            # Run specific test class
./mvnw test -Dtest=ClassName#methodName # Run specific test method
./mvnw verify                           # Run integration tests

# Run (starts on http://localhost:9966/petclinic/)
./mvnw spring-boot:run                                            # Default: H2 + Spring Data JPA
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2,jpa          # H2 + JPA
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql,jdbc      # MySQL + JDBC
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres,spring-data-jpa  # PostgreSQL + Spring Data JPA

# Swagger UI (when running)
# http://localhost:9966/petclinic/swagger-ui/index.html

# Dependencies
./mvnw dependency:tree                  # Show dependency tree
./mvnw versions:display-dependency-updates  # Check for updates

# OpenAPI code regeneration (happens automatically during compile)
./mvnw generate-sources                 # Regenerate DTOs and API interfaces from openapi.yml
```

## Coding Conventions

### OpenAPI-First Development
- **DTOs are generated** from `src/main/resources/openapi.yml` -- never hand-write DTO classes
- To add or modify a DTO, edit `openapi.yml` and run `./mvnw generate-sources`
- Generated code lands in `target/generated-sources/openapi/` -- never edit generated files
- API interfaces (e.g., `OwnersApi`) are also generated; controllers implement them
- When adding a new endpoint, add it to `openapi.yml` first, then implement the generated interface

### MapStruct Mappers
- All entity-to-DTO mapping uses MapStruct interfaces in the `mapper/` package
- Mappers are annotated with `@Mapper` and auto-discovered by Spring
- When adding a new entity, create a corresponding mapper interface

### Java Style
- Use constructor injection (never field injection with `@Autowired`)
- Entity hierarchy: `BaseEntity` (id) -> `NamedEntity` (name), `Person` (firstName, lastName) -> `Owner`, `Vet`
- Use `jakarta.persistence` annotations (not `javax`)
- Use `jakarta.validation.constraints` for bean validation on entities

### REST API Design
- Base path: `/api/{resource}` (plural nouns)
- Controllers implement generated API interfaces (e.g., `OwnerRestController implements OwnersApi`)
- Use `@PreAuthorize("hasRole(@roles.ROLE_NAME)")` for method-level security
- Use `@CrossOrigin(exposedHeaders = "errors, content-type")` on controllers
- Return `ResponseEntity<T>` for explicit status control
- Return proper status codes: 200 OK, 201 Created, 204 No Content, 404 Not Found, 400 Bad Request

### Service Layer
- `ClinicService` acts as a facade for all domain operations
- `ClinicServiceImpl` delegates to individual repository interfaces
- Use `@Transactional` on service methods; use `readOnly = true` for queries
- Repository interface methods may throw `DataAccessException`

### Persistence Profiles
- Three repository implementations exist; only one is active at a time:
  - `jdbc` -- plain JDBC with `NamedParameterJdbcTemplate` and row mappers
  - `jpa` -- JPA with `EntityManager` directly
  - `spring-data-jpa` -- Spring Data JPA (default)
- Selected via `spring.profiles.active` in `application.properties`

### Testing
- Controller tests use `@SpringBootTest` + `@WebAppConfiguration` + standalone `MockMvc` setup
- Use `@MockitoBean` to mock `ClinicService` in controller tests
- Use `@WithMockUser(roles = "OWNER_ADMIN")` for security context in tests
- Use BDD-style Mockito: `given(...).willReturn(...)` with `BDDMockito`
- Service integration tests extend abstract base classes per persistence profile

### Security (FSI Requirements)
- Security is toggled via `petclinic.security.enable` property (default: `false`)
- When enabled, basic auth is configured in `BasicAuthenticationConfig`
- When disabled, `DisableSecurityConfig` permits all requests
- Roles are defined as constants in `Roles.java` (OWNER_ADMIN, VET_ADMIN, ADMIN)
- Never hardcode credentials, tokens, or secrets
- Use environment variables or Spring Config Server for secrets
- Validate ALL user input
- Use parameterized queries (JPA/JDBC implementations handle this)
- Follow OWASP Top 10 guidelines

## Output Examples

### Controller Example (OwnerRestController)
```java
@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class OwnerRestController implements OwnersApi {

    private final ClinicService clinicService;
    private final OwnerMapper ownerMapper;
    private final PetMapper petMapper;
    private final VisitMapper visitMapper;

    public OwnerRestController(ClinicService clinicService,
                               OwnerMapper ownerMapper,
                               PetMapper petMapper,
                               VisitMapper visitMapper) {
        this.clinicService = clinicService;
        this.ownerMapper = ownerMapper;
        this.petMapper = petMapper;
        this.visitMapper = visitMapper;
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<List<OwnerDto>> listOwners(String lastName) {
        Collection<Owner> owners;
        if (lastName != null) {
            owners = this.clinicService.findOwnerByLastName(lastName);
        } else {
            owners = this.clinicService.findAllOwners();
        }
        if (owners.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ownerMapper.toOwnerDtoCollection(owners), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<OwnerDto> getOwner(Integer ownerId) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ownerMapper.toOwnerDto(owner), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<OwnerDto> addOwner(OwnerFieldsDto ownerFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        Owner owner = ownerMapper.toOwner(ownerFieldsDto);
        this.clinicService.saveOwner(owner);
        OwnerDto ownerDto = ownerMapper.toOwnerDto(owner);
        headers.setLocation(UriComponentsBuilder.newInstance()
            .path("/api/owners/{id}").buildAndExpand(owner.getId()).toUri());
        return new ResponseEntity<>(ownerDto, headers, HttpStatus.CREATED);
    }
}
```

### Service Example (ClinicServiceImpl)
```java
@Service
public class ClinicServiceImpl implements ClinicService {

    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final OwnerRepository ownerRepository;
    private final VisitRepository visitRepository;
    private final SpecialtyRepository specialtyRepository;
    private final PetTypeRepository petTypeRepository;

    public ClinicServiceImpl(
        PetRepository petRepository,
        VetRepository vetRepository,
        OwnerRepository ownerRepository,
        VisitRepository visitRepository,
        SpecialtyRepository specialtyRepository,
        PetTypeRepository petTypeRepository) {
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
        this.ownerRepository = ownerRepository;
        this.visitRepository = visitRepository;
        this.specialtyRepository = specialtyRepository;
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Owner findOwnerById(int id) throws DataAccessException {
        return findEntityById(() -> ownerRepository.findById(id));
    }

    @Override
    @Transactional
    public void saveOwner(Owner owner) throws DataAccessException {
        ownerRepository.save(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException {
        return ownerRepository.findByLastName(lastName);
    }
}
```

### MapStruct Mapper Example (OwnerMapper)
```java
@Mapper(uses = PetMapper.class)
public interface OwnerMapper {

    OwnerDto toOwnerDto(Owner owner);

    Owner toOwner(OwnerDto ownerDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    Owner toOwner(OwnerFieldsDto ownerDto);

    List<OwnerDto> toOwnerDtoCollection(Collection<Owner> ownerCollection);

    Collection<Owner> toOwners(Collection<OwnerDto> ownerDtos);
}
```

### Test Example (OwnerRestControllerTests)
```java
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
class OwnerRestControllerTests {

    @Autowired
    private OwnerRestController ownerRestController;

    @Autowired
    private OwnerMapper ownerMapper;

    @MockitoBean
    private ClinicService clinicService;

    private MockMvc mockMvc;
    private List<OwnerDto> owners;

    @BeforeEach
    void initOwners() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownerRestController)
            .setControllerAdvice(new ExceptionControllerAdvice())
            .build();
        owners = new ArrayList<>();
        OwnerDto ownerWithPet = new OwnerDto();
        owners.add(ownerWithPet.id(1).firstName("George").lastName("Franklin")
            .address("110 W. Liberty St.").city("Madison").telephone("6085551023")
            .addPetsItem(getTestPetWithIdAndName(1, "Rosy")));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnerSuccess() throws Exception {
        given(this.clinicService.findOwnerById(1)).willReturn(ownerMapper.toOwner(owners.get(0)));
        this.mockMvc.perform(get("/api/owners/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("George"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnerNotFound() throws Exception {
        given(this.clinicService.findOwnerById(2)).willReturn(null);
        this.mockMvc.perform(get("/api/owners/2")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
```

## Boundaries — DO NOT

- **NEVER** modify `pom.xml` parent version without explicit approval
- **NEVER** add dependencies without justification
- **NEVER** commit secrets, passwords, or API keys
- **NEVER** use field injection (`@Autowired` on fields)
- **NEVER** hand-write DTO classes -- they are generated from `openapi.yml`
- **NEVER** edit files under `target/generated-sources/` -- they are regenerated on every build
- **NEVER** ignore test failures -- fix them before proceeding
- **NEVER** use `System.out.println` -- use SLF4J logging
- **NEVER** suppress exceptions silently
- **NEVER** use `@SuppressWarnings` without a comment explaining why
- **NEVER** create MCP configurations (blocked by policy)
- **NEVER** bypass the `ClinicService` facade from controllers -- always go through the service layer
- **NEVER** mix persistence implementations -- only one profile (jdbc, jpa, or spring-data-jpa) should be active

## Workflow

1. **Understand** the task fully before making changes
2. **Check** existing tests pass: `./mvnw test`
3. **If adding/modifying API endpoints**, update `openapi.yml` first, then run `./mvnw generate-sources`
4. **Implement** the change following conventions above (implement generated interfaces, use MapStruct mappers)
5. **Write tests** for all new code (use `@WithMockUser` for security, `@MockitoBean` for service mocking)
6. **Verify** all tests pass: `./mvnw test`
7. **Review** the diff: `git diff`
8. **Commit** with a clear message following conventional commits
