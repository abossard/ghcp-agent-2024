---
name: api-development
description: >
  REST API development skill for Spring Boot. Covers endpoint design, request/response
  modeling, validation, error handling, and API documentation. USE FOR: REST endpoints,
  API design, request validation, error handling, HTTP status codes, OpenAPI.
---

# API Development Skill

## Procedure

### Creating a REST Endpoint

1. **Design the API contract first**
   - Define URL path (plural nouns: `/api/items`)
   - Choose HTTP methods (GET, POST, PUT, DELETE)
   - Define request/response DTOs
   - Plan error responses

2. **Create the Entity/Model**
   ```java
   @Entity
   @Table(name = "items")
   public record Item(
       @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id,
       @NotBlank @Size(max = 255) String name,
       String description
   ) {}
   ```

3. **Create the Repository**
   ```java
   @Repository
   public interface ItemRepository extends JpaRepository<Item, Long> {
       List<Item> findByNameContainingIgnoreCase(String name);
   }
   ```

4. **Create the Service**
   ```java
   @Service
   public class ItemService {
       private final ItemRepository repository;
       public ItemService(ItemRepository repository) {
           this.repository = repository;
       }
       // Business logic here
   }
   ```

5. **Create the Controller**
   ```java
   @RestController
   @RequestMapping("/api/items")
   public class ItemController {
       // GET /api/items        → list all
       // GET /api/items/{id}   → get by ID
       // POST /api/items       → create
       // PUT /api/items/{id}   → update
       // DELETE /api/items/{id} → delete
   }
   ```

### HTTP Status Codes
| Code | When to Use                              |
|------|------------------------------------------|
| 200  | Successful GET, PUT                      |
| 201  | Successful POST (resource created)       |
| 204  | Successful DELETE (no content)           |
| 400  | Validation error, bad request            |
| 404  | Resource not found                       |
| 409  | Conflict (duplicate, stale data)         |
| 500  | Unexpected server error                  |

### Testing the API
```bash
# Health check
curl -s http://localhost:8080/api/health | jq .

# List all
curl -s http://localhost:8080/api/items | jq .

# Create
curl -s -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{"name":"test","description":"a test item"}' | jq .

# Get by ID
curl -s http://localhost:8080/api/items/1 | jq .

# Delete
curl -s -X DELETE http://localhost:8080/api/items/1
```
