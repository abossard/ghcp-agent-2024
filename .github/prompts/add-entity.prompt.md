---
description: "Add a new JPA entity with repository, validation, and database migration"
agent: agent
tools:
  - codebase
  - terminal
  - editFiles
---

# Add New Entity

Create a new JPA entity with full persistence support.

## What to Generate

1. **Entity Class** in `src/main/java/com/example/demo/model/`
   - Use `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
   - Add Bean Validation: `@NotBlank`, `@NotNull`, `@Size`, etc.
   - Include audit fields if needed: `createdAt`, `updatedAt`

2. **Repository Interface** in `src/main/java/com/example/demo/repository/`
   - Extend `JpaRepository<Entity, Long>`
   - Add custom finder methods as needed

3. **Test Data** in `src/test/resources/data.sql` (optional)
   - INSERT statements for test data

## Conventions
- Table name: lowercase plural (`items`, `orders`)
- Column names: snake_case
- Use `@Column(nullable = false)` for required fields
- Use `@Enumerated(EnumType.STRING)` for enum fields
- Index frequently queried columns with `@Table(indexes = ...)`

## Verify
Run `./mvnw test` to confirm H2 schema generation works.
