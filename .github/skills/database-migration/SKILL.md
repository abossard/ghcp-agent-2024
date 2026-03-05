---
name: database-migration
description: >
  Database schema management and migration for Spring Boot with JPA/Hibernate.
  Covers entity design, schema evolution, H2 development database, and production 
  migration strategies. USE FOR: database schema, entity changes, migrations, 
  JPA configuration, H2 console, schema evolution.
---

# Database Migration Skill

## Procedure

### Development (H2)
The project uses H2 in-memory database for development with `ddl-auto: update`.

```bash
# Start the application
./mvnw spring-boot:run

# Access H2 Console
# URL: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# Username: sa
# Password: (empty)
```

### Entity Design Patterns

#### Basic Entity
```java
@Entity
@Table(name = "items", indexes = {
    @Index(name = "idx_item_name", columnList = "name")
})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

#### Relationships
```java
// One-to-Many
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Child> children = new ArrayList<>();

// Many-to-One
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "parent_id", nullable = false)
private Parent parent;

// Many-to-Many
@ManyToMany
@JoinTable(name = "item_tags",
    joinColumns = @JoinColumn(name = "item_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
private Set<Tag> tags = new HashSet<>();
```

### Schema Verification
```bash
# Check generated schema
./mvnw spring-boot:run
# Then check H2 console for table structure

# Run tests to verify schema
./mvnw test
```

### Production Migration Strategy
For production, switch from `ddl-auto: update` to a proper migration tool:
1. **Flyway**: Add `spring-boot-starter-flyway` and create SQL scripts in `src/main/resources/db/migration/`
2. **Liquibase**: Add `spring-boot-starter-liquibase` and create changelogs

```xml
<!-- Flyway -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-flyway</artifactId>
</dependency>
```
