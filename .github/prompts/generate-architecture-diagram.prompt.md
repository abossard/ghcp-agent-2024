---
description: "Generate an architecture diagram from the Spring Boot project structure"
agent: "A24 Diagrammer"
---

# Generate Architecture Diagram

Analyze the Spring Boot project and generate a professional architecture
diagram as an SVG using Mermaid showing all layers and infrastructure.

## Steps

1. Read `.github/skills/diagramming/SKILL.md` for Mermaid patterns
2. Read `pom.xml` → identify dependencies (database, cache, messaging, etc.)
3. Read `application.yml` → identify datasource, ports, profiles
4. Scan `src/main/java/**/controller/` → map REST API endpoints
5. Scan `src/main/java/**/service/` → map business logic layer
6. Scan `src/main/java/**/repository/` → map data access layer
7. Choose the best-fitting diagram pattern from the skill:
   - Local dev: Spring Boot 3-tier pattern
   - Azure deployment: Azure architecture pattern
8. Generate `docs/diagrams/architecture.mmd`
9. Render: `npx --yes @mermaid-js/mermaid-cli -i docs/diagrams/architecture.mmd -o docs/diagrams/architecture.svg -q`
10. Verify `docs/diagrams/architecture.svg` exists and is non-empty

## Quality Check

- [ ] All controllers/services/repositories represented
- [ ] Database technology matches actual config (H2/PostgreSQL)
- [ ] Diagram direction and layout are readable
- [ ] No placeholder components — only actual project resources
