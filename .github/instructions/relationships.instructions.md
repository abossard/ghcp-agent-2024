---
applyTo: "src/main/java/**/model/**"
---

# JPA Relationship Instructions

When implementing entities with relationships, follow these patterns. Use the PetClinic domain as the reference implementation.

## Relationship Patterns

### @ManyToOne (child side)
The "many" side owns the relationship. Always specify the join column explicitly.

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "owner_id", nullable = false)
private Owner owner;
```

**Rules:**
- Always use `FetchType.LAZY` to avoid N+1 queries
- Use `nullable = false` when the relationship is required
- Name the join column `{entity}_id` (snake_case)

**Reference:** `Pet.owner` → `Owner` (`@ManyToOne`)

### @OneToMany (parent side)
The "one" side is the inverse/non-owning side. Always use `mappedBy`.

```java
@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Pet> pets = new ArrayList<>();
```

**Rules:**
- Always specify `mappedBy` pointing to the field name on the owning side
- Use `CascadeType.ALL` when the child's lifecycle is managed by the parent
- Initialize collections inline (`= new ArrayList<>()`) to avoid NPE
- Always use `FetchType.LAZY`

**Reference:** `Owner.pets` → `List<Pet>` (`@OneToMany`)

### @ManyToMany
Use a join table. One side is the owner, the other uses `mappedBy`.

```java
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(
        name = "vet_specialties",
        joinColumns = @JoinColumn(name = "vet_id"),
        inverseJoinColumns = @JoinColumn(name = "specialty_id")
)
private Set<Specialty> specialties = new HashSet<>();
```

**Rules:**
- Name join table `{entity1}_{entity2}` (snake_case, alphabetical or logical order)
- Use `Set<>` for `@ManyToMany` to avoid duplicate issues with Hibernate bag semantics
- Use `FetchType.LAZY`
- Only ONE side owns the relationship (`@JoinTable`); the other uses `mappedBy`

**Reference:** `Vet.specialties` → `Set<Specialty>` (`@ManyToMany`)

## DTO Mapping with Relationships

### Rule: Never expose entities with lazy relationships in API responses

DTOs are generated from `src/main/resources/openapi.yml`. Use MapStruct mappers in `org.springframework.samples.petclinic.mapper` for entity ↔ DTO conversion.

**For the owning side (@ManyToOne):** The OpenAPI schema includes nested DTO references or ID fields.

```yaml
# In openapi.yml — PetDto references PetTypeDto and OwnerDto
PetDto:
  properties:
    type:
      $ref: '#/components/schemas/PetTypeDto'
    ownerId:
      type: integer
```

**MapStruct handles the conversion:**
```java
@Mapper(componentModel = "spring", uses = {PetTypeMapper.class, VisitMapper.class})
public interface PetMapper {
    PetDto toDto(Pet pet);
    Pet toEntity(PetDto petDto);
}
```

**For the inverse side (@OneToMany):** Include in the DTO via MapStruct `uses` clause or use a separate endpoint.

## Entity Build Order (Dependency Chain)

When implementing multiple related entities, follow this order:

1. **Independent entities first** (no foreign keys): `PetType`, `Specialty`
2. **Entities with only outgoing refs**: `Owner` (has `@OneToMany` but no `@ManyToOne`)
3. **Entities with incoming refs**: `Vet` (depends on `Specialty` via `@ManyToMany`)
4. **Dependent entities**: `Pet` (depends on `Owner` + `PetType`)
5. **Most dependent entities last**: `Visit` (depends on `Pet`)

## Reference Implementation Files

All entities are in `org.springframework.samples.petclinic.model`, mappers in `org.springframework.samples.petclinic.mapper`.

| Relationship | From Entity | To Entity | Type | Mapper |
|---|---|---|---|---|
| Owner → Pets | `Owner.java` | `Pet.java` | `@OneToMany` / `@ManyToOne` | `OwnerMapper`, `PetMapper` |
| Pet → PetType | `Pet.java` | `PetType.java` | `@ManyToOne` | `PetMapper`, `PetTypeMapper` |
| Pet → Visits | `Pet.java` | `Visit.java` | `@OneToMany` / `@ManyToOne` | `PetMapper`, `VisitMapper` |
| Vet → Specialties | `Vet.java` | `Specialty.java` | `@ManyToMany` | `VetMapper`, `SpecialtyMapper` |
