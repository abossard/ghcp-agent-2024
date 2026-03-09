---
applyTo: "**/*.java"
---

# Feature Implementation Pipeline

When asked to implement a feature, follow this EXACT pipeline. Do not skip steps. Do not ask for permission between steps.

## Pipeline

### Phase 1: Understand
1. Read the feature specification carefully
2. Identify: entity name, fields, validation rules, relationships, business logic
3. Read existing code patterns (look at Owner for complex entities with relationships, PetType for simple entities without relationships)

### Phase 2: Build (in this order)
1. **Entity** → `src/main/java/org/springframework/samples/petclinic/model/{Name}.java` (extend BaseEntity, NamedEntity, or Person as appropriate)
2. **Repository** → `src/main/java/org/springframework/samples/petclinic/repository/{Name}Repository.java`
3. **MapStruct Mapper** → `src/main/java/org/springframework/samples/petclinic/mapper/{Name}Mapper.java` (entity ↔ DTO conversion)
4. **DTOs** → Generated from `src/main/resources/openapi.yml` (add schema definition, then rebuild)
5. **Service** → Add methods to `ClinicService` interface and `ClinicServiceImpl`
6. **Controller** → `src/main/java/org/springframework/samples/petclinic/rest/controller/{Name}RestController.java`

### Phase 3: Test (in this order)
7. **Service Test** → `src/test/java/org/springframework/samples/petclinic/service/{Name}ServiceTest.java`
8. **Controller Test** → `src/test/java/org/springframework/samples/petclinic/rest/controller/{Name}RestControllerTest.java`

### Phase 4: Verify
9. Run `./mvnw clean test` — all tests MUST pass
10. If failures: fix immediately, re-run until green

### Phase 5: Report
11. List all files created
12. List all tests and their status
13. Show the API endpoints created

## Reference Implementations

### Owner (complex entity with relationships)
The `Owner` feature is the reference for entities that have relationships to other entities:
- Entity: `src/main/java/org/springframework/samples/petclinic/model/Owner.java` (extends Person)
- Repository: `src/main/java/org/springframework/samples/petclinic/repository/OwnerRepository.java`
- Mapper: `src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java` (MapStruct)
- Service: `ClinicService` / `ClinicServiceImpl` (owner-related methods)
- Controller: `src/main/java/org/springframework/samples/petclinic/rest/controller/OwnerRestController.java`
- DTOs: Generated from `src/main/resources/openapi.yml` (OwnerDto, OwnerFieldsDto)

### PetType (simple entity without relationships)
The `PetType` feature is the reference for simple standalone entities:
- Entity: `src/main/java/org/springframework/samples/petclinic/model/PetType.java` (extends NamedEntity)
- Repository: `src/main/java/org/springframework/samples/petclinic/repository/PetTypeRepository.java`
- Mapper: `src/main/java/org/springframework/samples/petclinic/mapper/PetTypeMapper.java` (MapStruct)
- Service: `ClinicService` / `ClinicServiceImpl` (petType-related methods)
- Controller: `src/main/java/org/springframework/samples/petclinic/rest/controller/PetTypeRestController.java`
- DTOs: Generated from `src/main/resources/openapi.yml` (PetTypeDto, PetTypeFieldsDto)

### Shared
- OpenAPI spec: `src/main/resources/openapi.yml` (defines all DTOs and API contracts)
- Entity hierarchy: `BaseEntity` → `NamedEntity` → ... and `BaseEntity` → `Person` → ...
- Service layer: `ClinicService` (interface) + `ClinicServiceImpl` (implementation)
- Error Handler: `src/main/java/org/springframework/samples/petclinic/rest/advice/ExceptionControllerAdvice.java`
