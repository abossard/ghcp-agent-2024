---
description: "Generate a JPA Entity-Relationship Diagram from the project's @Entity classes"
agent: "A24 Diagrammer"
---

# Generate JPA ERD

Scan all `@Entity` classes in the project and generate a professional
Entity-Relationship Diagram as an SVG using Mermaid's `erDiagram`.

## Steps

1. Read `.github/skills/diagramming/SKILL.md` for Mermaid ERD patterns
2. Find all Java files in `src/main/java/**/model/` containing `@Entity`
3. For each entity, extract and map to Mermaid `erDiagram`:
   - Class name / `@Table` name → entity name
   - `@Id` fields → `PK` marker
   - `@Column(unique)` fields → `UK` marker
   - `@ManyToOne` / `@OneToMany` / `@ManyToMany` → `FK` marker + relationship arrows
   - `@Enumerated` fields → `VARCHAR` with `"enum"` comment
4. Generate `docs/diagrams/erd.mmd`
5. Render: `npx --yes @mermaid-js/mermaid-cli -i docs/diagrams/erd.mmd -o docs/diagrams/erd.svg -q`
6. Verify `docs/diagrams/erd.svg` exists and is non-empty

## Quality Check

- [ ] Every `@Entity` class appears in the erDiagram
- [ ] All `@Id` fields have `PK` marker
- [ ] All relationship annotations rendered as arrows with correct cardinality
- [ ] Diagram renders without errors
