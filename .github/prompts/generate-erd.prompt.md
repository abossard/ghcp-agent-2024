---
description: "Generate a JPA Entity-Relationship Diagram from the project's @Entity classes"
agent: "A24 Diagrammer"
---

# Generate JPA ERD

Scan all `@Entity` classes in the project and generate a professional
Entity-Relationship Diagram as a PNG image.

## Steps

1. Read `.github/skills/diagramming/SKILL.md` for ERD generation patterns
2. Find all Java files in `src/main/java/**/model/` containing `@Entity`
3. For each entity, extract:
   - Class name → table name
   - `@Id` fields → primary keys (🔑)
   - `@Column` fields → columns with types and constraints
   - `@ManyToOne` / `@OneToMany` / `@ManyToMany` → relationships with cardinality
   - `@Enumerated` fields → enum types
4. Generate `docs/diagrams/erd.py` using the Graphviz ERD template from the skill
5. Install prerequisites if missing: `pip install graphviz`
6. Run: `cd docs/diagrams && python3 erd.py`
7. Verify `docs/diagrams/erd.png` exists and is non-empty

## Quality Check

- [ ] Every `@Entity` class appears as a table in the ERD
- [ ] All `@Id` fields marked as PK
- [ ] All relationship annotations rendered as arrows with correct cardinality
- [ ] Diagram renders without errors
