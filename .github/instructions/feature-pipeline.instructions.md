---
applyTo: "**/*.java"
---

# Feature Implementation Pipeline

When asked to implement a feature, follow this EXACT pipeline. Do not skip steps. Do not ask for permission between steps.

## Pipeline

### Phase 1: Understand
1. Read the feature specification carefully
2. Identify: entity name, fields, validation rules, relationships, business logic
3. Read existing code patterns (look at Greeting as the reference implementation)

### Phase 2: Build (in this order)
1. **Entity** → `src/main/java/com/example/demo/model/{Name}.java`
2. **Repository** → `src/main/java/com/example/demo/repository/{Name}Repository.java`
3. **Request DTO** → `src/main/java/com/example/demo/controller/dto/Create{Name}Request.java`
4. **Response DTO** → `src/main/java/com/example/demo/controller/dto/{Name}Response.java`
5. **Service** → `src/main/java/com/example/demo/service/{Name}Service.java`
6. **Controller** → `src/main/java/com/example/demo/controller/{Name}Controller.java`

### Phase 3: Test (in this order)
7. **Service Test** → `src/test/java/com/example/demo/service/{Name}ServiceTest.java`
8. **Controller Test** → `src/test/java/com/example/demo/controller/{Name}ControllerTest.java`

### Phase 4: Verify
9. Run `./mvnw clean test` — all tests MUST pass
10. If failures: fix immediately, re-run until green

### Phase 5: Report
11. List all files created
12. List all tests and their status
13. Show the API endpoints created

## Reference Implementation
The `Greeting` feature is the reference. Look at these files to understand the pattern:
- Entity: `src/main/java/com/example/demo/model/Greeting.java`
- Repository: `src/main/java/com/example/demo/repository/GreetingRepository.java`
- Service: `src/main/java/com/example/demo/service/GreetingService.java`
- Controller: `src/main/java/com/example/demo/controller/GreetingController.java`
- Request DTO: `src/main/java/com/example/demo/controller/dto/CreateGreetingRequest.java`
- Response DTO: `src/main/java/com/example/demo/controller/dto/GreetingResponse.java`
- Service Test: `src/test/java/com/example/demo/service/GreetingServiceTest.java`
- Controller Test: `src/test/java/com/example/demo/controller/GreetingControllerTest.java`
- Error Handler: `src/main/java/com/example/demo/config/GlobalExceptionHandler.java`
