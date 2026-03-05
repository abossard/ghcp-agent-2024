---
description: "Create a new REST endpoint with controller, service, repository, entity, and tests"
agent: agent
tools:
  - codebase
  - terminal
  - editFiles
---

# Create New REST Endpoint

Create a complete REST API endpoint for the resource described below.

## What to Generate

1. **Entity** in `src/main/java/com/example/demo/model/` — JPA `@Entity` using a Java record if read-only, or a class if mutable
2. **Repository** in `src/main/java/com/example/demo/repository/` — extends `JpaRepository`
3. **Service** in `src/main/java/com/example/demo/service/` — business logic with constructor injection
4. **Controller** in `src/main/java/com/example/demo/controller/` — REST endpoints (`@RestController`)
5. **Service Test** in `src/test/java/com/example/demo/service/` — unit test with Mockito
6. **Controller Test** in `src/test/java/com/example/demo/controller/` — integration test with MockMvc

## Conventions
- Use Java 21 features (records, var, text blocks)
- Constructor injection only
- Return `Optional` from service methods
- Use `@Valid` on request bodies
- Follow HTTP method conventions (GET/POST/PUT/DELETE)
- Return proper status codes (200, 201, 204, 404)

## After Creating
Run `./mvnw test` to verify all tests pass.
