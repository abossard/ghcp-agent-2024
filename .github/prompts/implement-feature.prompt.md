---
description: "Implement a complete feature from spec — entity, DTO, repository, service, controller, tests, and error handling"
---

# Implement Feature from Specification

Implement the feature COMPLETELY and AUTONOMOUSLY following the spring-petclinic-rest project patterns.
Follow the feature-pipeline.instructions.md for the exact pipeline.
Use `Owner`/`OwnerRestController` as the reference for complex entities with relationships, or `PetType`/`PetTypeRestController` for simple entities without relationships.

Key patterns:
- **OpenAPI-first**: Define DTOs in `src/main/resources/openapi.yml`, then generate with `./mvnw generate-sources`
- **MapStruct mappers**: Create mapper in `org.springframework.samples.petclinic.mapper` for entity ↔ DTO conversion
- **Service layer**: Add methods to `ClinicService` interface and `ClinicServiceImpl` (do NOT create per-entity services)
- **Controllers**: REST controllers in `org.springframework.samples.petclinic.rest.controller`
- **Entity hierarchy**: Extend `BaseEntity`, `NamedEntity`, or `Person` as appropriate

Run `./mvnw clean test` after implementation — all tests must pass.
