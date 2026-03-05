---
applyTo:
  - "src/main/java/**/controller/**"
  - "src/main/java/**/controller/dto/**"
---

# DTO & Controller Pattern Instructions

## ALWAYS Use DTOs — Never Expose Entities in API

### Request DTO (record)
```java
package com.example.demo.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Create{Name}Request(
        @NotBlank(message = "{field} is required")
        @Size(max = 255, message = "{field} must be at most 255 characters")
        String fieldName
) {
    // Add helper methods for defaults if needed
}
```

### Response DTO (record with factory)
```java
package com.example.demo.controller.dto;

import com.example.demo.model.{Name};
import java.util.List;

public record {Name}Response(
        Long id,
        String fieldName
) {
    public static {Name}Response from({Name} entity) {
        return new {Name}Response(entity.getId(), entity.getFieldName());
    }

    public static List<{Name}Response> fromList(List<{Name}> entities) {
        return entities.stream().map({Name}Response::from).toList();
    }
}
```

### Controller Pattern
```java
@RestController
@RequestMapping("/api/{resources}")
public class {Name}Controller {
    private final {Name}Service service;

    public {Name}Controller({Name}Service service) {
        this.service = service;
    }

    @GetMapping
    public List<{Name}Response> getAll() {
        return {Name}Response.fromList(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<{Name}Response> getById(@PathVariable Long id) {
        return service.findById(id)
                .map({Name}Response::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public {Name}Response create(@Valid @RequestBody Create{Name}Request request) {
        var entity = new {Name}(request.fieldName());
        return {Name}Response.from(service.create(entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
```

## Validation Error Handling
The `GlobalExceptionHandler` in `config/` automatically converts `@Valid` failures into structured JSON:
```json
{
    "timestamp": "2026-03-05T12:00:00Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "errors": { "fieldName": "field is required" },
    "path": "/api/resources"
}
```
