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

## Configuration
- Use `application.yml` (not .properties)
- Use Spring profiles for environment-specific config
- Externalize secrets via environment variables: `${DB_PASSWORD:default}`
- Use `@ConfigurationProperties` for typed config binding

## REST Controllers
```java
@RestController
@RequestMapping("/api/{resource}")  // plural nouns
public class ResourceController {
    // Constructor injection only
    // Return ResponseEntity<T> for status control
    // Use @Valid on @RequestBody
}
```

## Spring Data JPA
- Extend `JpaRepository<Entity, IdType>`
- Use derived query methods where possible
- Use `@Query` for complex queries with JPQL
- Never use native SQL unless absolutely necessary

## Error Handling
- Use `@ControllerAdvice` with `@ExceptionHandler` methods
- Return structured error JSON: `{ timestamp, status, error, message, path }`
- Map business exceptions to proper HTTP status codes

## Actuator
- Expose only needed endpoints: health, info, metrics
- Keep sensitive endpoints behind authentication in production
