---
description: "Add a new JPA entity with repository, validation, and database migration"
---

# Add New Entity

Create a new JPA entity with full persistence support.

1. **Entity Class** in `model/` — `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, Bean Validation
2. **Repository Interface** in `repository/` — extend `JpaRepository<Entity, Long>`
3. **Test Data** in `src/test/resources/data.sql` (optional)

Use the `Greeting` entity as reference. Run `./mvnw test` to confirm.
