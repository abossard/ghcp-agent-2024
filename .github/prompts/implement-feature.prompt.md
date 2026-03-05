---
description: "Implement a complete feature from spec — entity, DTO, repository, service, controller, tests, and error handling"
agent: agent
tools:
  - codebase
  - terminal
  - editFiles
  - search
  - problems
  - usages
---

# Implement Feature from Specification

You are given a feature specification. Implement it COMPLETELY and AUTONOMOUSLY following the project patterns.

## Your Implementation Checklist

Execute EVERY step in order. Do not skip any step. Do not ask for confirmation between steps.

### Step 1: Analyze Existing Patterns
- Read `AGENTS.md` for project conventions
- Read an existing entity (e.g., `Greeting.java`) to see the JPA pattern
- Read an existing controller (e.g., `GreetingController.java`) to see the REST pattern
- Read existing DTOs (e.g., `CreateGreetingRequest.java`, `GreetingResponse.java`) for the DTO pattern
- Read existing tests to see the testing pattern

### Step 2: Create the Entity
- Location: `src/main/java/com/example/demo/model/{EntityName}.java`
- Use `@Entity`, `@Table`, `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- Add `@NotBlank`, `@Size`, `@NotNull` validation annotations
- Protected no-arg constructor (JPA requirement)
- Public constructor with business fields
- Getters and setters

### Step 3: Create the Repository
- Location: `src/main/java/com/example/demo/repository/{EntityName}Repository.java`
- Extend `JpaRepository<EntityName, Long>`
- Add custom finder methods as needed

### Step 4: Create Request/Response DTOs
- Location: `src/main/java/com/example/demo/controller/dto/`
- `Create{EntityName}Request` — Java record with validation annotations
- `{EntityName}Response` — Java record with `static from(Entity)` factory method and `static fromList()` method

### Step 5: Create the Service
- Location: `src/main/java/com/example/demo/service/{EntityName}Service.java`
- Constructor injection of repository
- Methods: `findAll()`, `findById(Long)`, `create(Entity)`, `delete(Long)`
- Return `Optional<T>` for nullable results
- Add any business-specific methods from the spec

### Step 6: Create the Controller
- Location: `src/main/java/com/example/demo/controller/{EntityName}Controller.java`
- `@RestController` + `@RequestMapping("/api/{resources}")`
- Use DTOs for request/response (never expose entities directly)
- `@Valid` on all `@RequestBody` params
- Proper HTTP status codes: 200, 201, 204, 404

### Step 7: Write Service Unit Tests
- Location: `src/test/java/com/example/demo/service/{EntityName}ServiceTest.java`
- `@ExtendWith(MockitoExtension.class)`, `@Mock` repo, `@InjectMocks` service
- Test: findAll, findById (found + not found), create, delete
- Use AssertJ assertions

### Step 8: Write Controller Integration Tests
- Location: `src/test/java/com/example/demo/controller/{EntityName}ControllerTest.java`
- `@SpringBootTest` + `@AutoConfigureMockMvc`
- Test: GET all, GET by id (found + not found), POST (valid + invalid), DELETE
- Test validation errors return 400 with structured error body

### Step 9: Verify
```bash
./mvnw clean test
```
ALL tests must pass. If any fail, fix them before proceeding.

### Step 10: Summary
Report what was created:
- List of files created/modified
- Number of tests written and their pass status
- Any decisions made during implementation
