---
applyTo:
  - "src/main/java/**/*.java"
  - "src/main/resources/**"
  - "pom.xml"
---

# Spring Boot Instructions

## Architecture

- Follow layered architecture: Controller → Service → Repository
- Controllers handle HTTP, delegate to services
- Services contain business logic, delegate to repositories
- Repositories handle data access via Spring Data JPA

## Dependency Injection

- ALWAYS use constructor injection
- NEVER use `@Autowired` on fields
- Single-constructor classes don't need `@Autowired` annotation

## Service Layer

- Use service interface + implementation pattern: `ClinicService` (interface) + `ClinicServiceImpl`
- All service methods for the clinic domain live in `ClinicService` — do NOT create per-entity service classes
- Annotate implementation with `@Service` and `@Transactional`

## Configuration

- Use `application.properties` (not .yml)
- Server runs on port 9966
- Use Spring profiles for environment-specific config (e.g., `spring`, `jdbc`, `jpa`)
- Externalize secrets via environment variables: `${DB_PASSWORD:default}`
- Spring Security is included but disabled by default

## REST Controllers

```java
@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/{resource}")  // plural nouns
public class {Resource}RestController {
    // Constructor injection only
    // Inject ClinicService and the appropriate MapStruct mapper
    // Return ResponseEntity<T> for status control
    // Use @Valid on @RequestBody
    // Controllers live in org.springframework.samples.petclinic.rest.controller
}
```

## Spring Data JPA

- Extend `JpaRepository<Entity, IdType>` (default persistence layer)
- Multiple persistence implementations available (Spring Data JPA, Spring JDBC, plain JPA)
- Use derived query methods where possible
- Use `@Query` for complex queries with JPQL
- Entities extend `BaseEntity` (provides id), `NamedEntity` (adds name), or `Person` (adds firstName/lastName)
- Never use native SQL unless absolutely necessary

## Error Handling

- Use `@ControllerAdvice` with `@ExceptionHandler` methods
- Return structured error JSON: `{ timestamp, status, error, message, path }`
- Map business exceptions to proper HTTP status codes

## Actuator

- Expose only needed endpoints: health, info, metrics
- Keep sensitive endpoints behind authentication in production
