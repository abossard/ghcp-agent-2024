---
applyTo: "src/main/java/**/rest/**"
---

# DTO & Controller Pattern Instructions

## OpenAPI-First: DTOs Are Generated — Never Write DTOs Manually

This project uses an **OpenAPI-first** approach. DTOs are auto-generated from `src/main/resources/openapi.yml` during the build. Do NOT create DTO classes by hand.

### The Pattern

1. **Define the schema** in `src/main/resources/openapi.yml` under `components/schemas`
2. **Run the build** (`./mvnw generate-sources`) to generate DTO classes
3. **Create a MapStruct mapper** for entity ↔ DTO conversion
4. **Use the mapper** in the REST controller

### Adding a New DTO

Add the schema to `openapi.yml`:
```yaml
components:
  schemas:
    {Name}Dto:
      type: object
      required:
        - fieldName
      properties:
        id:
          type: integer
          format: int32
          readOnly: true
        fieldName:
          type: string
          maxLength: 255
    {Name}FieldsDto:
      type: object
      required:
        - fieldName
      properties:
        fieldName:
          type: string
          maxLength: 255
```

### MapStruct Mapper
```java
package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.model.{Name};
import org.springframework.samples.petclinic.rest.dto.{Name}Dto;

@Mapper(componentModel = "spring")
public interface {Name}Mapper {
    {Name}Dto toDto({Name} entity);
    {Name} toEntity({Name}Dto dto);
    Collection<{Name}Dto> toDtos(Collection<{Name}> entities);
}
```

### Controller Pattern
```java
@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/{resources}")
public class {Name}RestController {
    private final ClinicService clinicService;
    private final {Name}Mapper {name}Mapper;

    public {Name}RestController(ClinicService clinicService, {Name}Mapper {name}Mapper) {
        this.clinicService = clinicService;
        this.{name}Mapper = {name}Mapper;
    }

    @GetMapping
    public ResponseEntity<List<{Name}Dto>> getAll() {
        List<{Name}Dto> dtos = new ArrayList<>({name}Mapper.toDtos(clinicService.findAll{Name}s()));
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<{Name}Dto> getById(@PathVariable int id) {
        {Name} entity = clinicService.find{Name}ById(id);
        return new ResponseEntity<>({name}Mapper.toDto(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<{Name}Dto> create(@Valid @RequestBody {Name}Dto dto) {
        {Name} entity = {name}Mapper.toEntity(dto);
        clinicService.save{Name}(entity);
        return new ResponseEntity<>({name}Mapper.toDto(entity), HttpStatus.CREATED);
    }
}
```

## Reference Implementations
- **Simple entity (no relationships)**: See `PetTypeMapper`, `PetTypeRestController`
- **Complex entity (with relationships)**: See `OwnerMapper`, `OwnerRestController`
- **Relationship DTOs**: See `relationships.instructions.md` for how to handle entity references in DTOs

## Validation Error Handling
The `ExceptionControllerAdvice` in `rest/advice/` handles error responses with `BindingResult` for validation errors.
