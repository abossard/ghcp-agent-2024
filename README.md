# 🚀 Agentic Spring Boot Starter — FSI Edition

> **Unlock agentic AI-assisted development with GitHub Copilot in restricted Financial Services (FSI) environments — no MCP required.**

This starter project demonstrates how to prepare a Spring Boot application — built around the classic **PetClinic** domain (Owners, Pets, Vets, Visits) — for maximum productivity with GitHub Copilot's Agent Mode, even when MCP (Model Context Protocol) is blocked by organizational policy.

## 🎯 What This Project Solves

In many FSI environments (like Swiss Re), developers face significant restrictions:
- ❌ MCP is blocked by policy
- ❌ Open-source AI tools are not allowed
- ❌ Premium tokens are limited
- ⚠️ WSL2 has incompatibility with Windows Copilot extensions
- ✅ Official VS Code + GitHub Copilot IS available
- ✅ Agent Mode IS enabled

**This starter shows how to maximize agentic development within these constraints** using only built-in VS Code + Copilot features: custom agents, skills, instructions, prompts, chat modes, and terminal tool automation.

## 📊 Architecture Overview

```mermaid
graph TB
    BLOCK["🚫 MCP Blocked · Open Code Blocked"]
    AVAIL["✅ VS Code + Copilot Agent Mode + CLI"]

    BLOCK -.->|"workaround"| AVAIL
    AVAIL --> AGENTS["📋 AGENTS.md"]

    AGENTS --> Agents["Agents<br/>Developer · Tester · Reviewer<br/>API Designer · Execution Lead"]
    AGENTS --> Config["Instructions + Skills + Prompts<br/>Java 21 · Spring Boot · Security<br/>Maven · Testing · Diagramming"]
    AGENTS --> Modes["Chat Modes<br/>Dev · Test · Review"]

    Agents --> APP["🌱 Spring Boot 3.4.x / Java 21<br/>PetClinic REST: Owner · Pet · Vet · Visit · PetType · Specialty<br/>Controller → MapStruct Mapper → ClinicService → Repository → Entity<br/>OpenAPI-first DTOs · 3 Repository Implementations"]
    Config --> APP
    Modes --> APP

    style BLOCK fill:#ff6b6b,stroke:#c0392b,color:#fff
    style AVAIL fill:#27ae60,stroke:#1e8449,color:#fff
    style AGENTS fill:#3498db,stroke:#2980b9,color:#fff
    style APP fill:#2c3e50,stroke:#1a252f,color:#fff
```

## 🔧 How Agentic Development Works (Without MCP)

```mermaid
flowchart TB
    A[Developer] --> B{Choose Mode}
    B -->|Dev| D[Spring Dev]
    B -->|Test| E[Test Mode]
    B -->|Review| F[Review Mode]

    D & E & F --> G[AGENTS.md → Instructions → Skills]
    G --> H[CLI: mvnw test · compile · git diff]
    H --> I[✅ Tests Pass · Build OK · Changes Reviewed]

    style D fill:#27ae60,color:#fff
    style E fill:#3498db,color:#fff
    style F fill:#e74c3c,color:#fff
```

## 📁 Project Structure

```
.
├── AGENTS.md                          # 🤖 Main agent instructions (read by ALL agents)
├── README.md                          # 📖 This file
├── pom.xml                            # 📦 Maven build configuration
│
├── .vscode/
│   ├── settings.json                  # ⚙️ Workspace settings (Copilot, Java, terminal tools)
│   └── extensions.json                # 📋 Recommended VS Code extensions
│
├── .github/
│   ├── copilot-instructions.md        # 📋 Project-wide Copilot instructions
│   │
│   ├── agents/                        # 🤖 Custom agent definitions
│   │   ├── spring-boot-developer.md   #    Full-stack Spring Boot developer
│   │   ├── test-engineer.md           #    Testing specialist
│   │   ├── security-reviewer.md       #    FSI security reviewer
│   │   ├── api-designer.md            #    REST API designer
│   │   ├── execution-lead.agent.md    #    🆕 Pipeline orchestrator
│   │   └── diagrammer.agent.md        #    🆕 Architecture & ERD diagrams
│   │
│   ├── instructions/                  # 📝 Context-specific instructions
│   │   ├── java.instructions.md       #    Java 21 coding style
│   │   ├── spring-boot.instructions.md#    Spring Boot patterns
│   │   ├── testing.instructions.md    #    Testing conventions
│   │   ├── security.instructions.md   #    FSI security requirements
│   │   ├── research-first.instructions.md  # 🆕 Research before implementation
│   │   └── quality-gates.instructions.md   # 🆕 Automated quality enforcement
│   │
│   ├── prompts/                       # ⚡ Reusable prompt templates
│   │   ├── new-rest-endpoint.prompt.md#    Create complete REST endpoint
│   │   ├── write-tests.prompt.md      #    Write tests for a class
│   │   ├── security-review.prompt.md  #    Run security audit
│   │   ├── refactor-service.prompt.md #    Refactor a service
│   │   ├── add-entity.prompt.md       #    Add JPA entity
│   │   ├── explain-codebase.prompt.md #    Explain architecture
│   │   ├── generate-erd.prompt.md     #    🆕 Generate JPA ERD diagram
│   │   └── generate-architecture-diagram.prompt.md # 🆕 Architecture diagram
│   │
│   ├── chatmodes/                     # 🎭 Custom chat modes
│   │   ├── spring-dev.chatmode.md     #    Development mode
│   │   ├── test-mode.chatmode.md      #    Testing mode
│   │   └── review-mode.chatmode.md    #    Code review mode
│   │
│   └── skills/                        # 🛠️ Agent skills
│       ├── maven-build/SKILL.md       #    Maven build automation
│       ├── spring-testing/SKILL.md    #    Spring Boot testing
│       ├── api-development/SKILL.md   #    REST API development
│       ├── database-migration/SKILL.md#    Database schema management
│       └── diagramming/SKILL.md       #    🆕 Architecture & ERD diagrams
│
├── docs/diagrams/                     # 📊 Generated diagram output
│   ├── architecture.mmd              #    Architecture diagram source (Mermaid)
│   ├── architecture.svg              #    Rendered architecture
│   ├── erd.mmd                       #    ERD source (Mermaid)
│   ├── erd.svg                       #    Rendered ERD
│   ├── class-diagram.mmd            #    Class diagram source (Mermaid)
│   └── class-diagram.svg            #    Rendered class diagram
│
├── docs/                              # 📚 Research & documentation
│   ├── research-notes.md              #    Deep research findings
│   ├── agentic-development-guide.md   #    How-to guide for teams
│   └── fsi-constraints-workarounds.md #    FSI-specific guidance
│
└── src/
    ├── main/
    │   ├── java/org/springframework/samples/petclinic/
    │   │   ├── PetClinicApplication.java
    │   │   ├── config/
    │   │   │   └── ApplicationConfig.java
    │   │   ├── mapper/                     # MapStruct mappers (entity <-> DTO)
    │   │   │   ├── OwnerMapper.java
    │   │   │   ├── PetMapper.java
    │   │   │   ├── PetTypeMapper.java
    │   │   │   ├── VetMapper.java
    │   │   │   ├── VisitMapper.java
    │   │   │   ├── SpecialtyMapper.java
    │   │   │   └── UserMapper.java
    │   │   ├── model/                      # JPA entities (BaseEntity hierarchy)
    │   │   │   ├── BaseEntity.java
    │   │   │   ├── NamedEntity.java
    │   │   │   ├── Person.java
    │   │   │   ├── Owner.java
    │   │   │   ├── Pet.java
    │   │   │   ├── PetType.java
    │   │   │   ├── Vet.java
    │   │   │   ├── Visit.java
    │   │   │   ├── Specialty.java
    │   │   │   ├── User.java
    │   │   │   └── Role.java
    │   │   ├── rest/                       # REST controllers
    │   │   │   ├── OwnerRestController.java
    │   │   │   ├── PetRestController.java
    │   │   │   ├── PetTypeRestController.java
    │   │   │   ├── VetRestController.java
    │   │   │   ├── VisitRestController.java
    │   │   │   ├── SpecialtyRestController.java
    │   │   │   └── UserRestController.java
    │   │   ├── rest/dto/                   # DTOs (generated from openapi.yml)
    │   │   │   ├── OwnerDto.java
    │   │   │   ├── PetDto.java
    │   │   │   ├── PetTypeDto.java
    │   │   │   ├── VetDto.java
    │   │   │   ├── VisitDto.java
    │   │   │   ├── SpecialtyDto.java
    │   │   │   └── UserDto.java
    │   │   ├── security/                   # Spring Security config
    │   │   ├── service/
    │   │   │   ├── ClinicService.java
    │   │   │   ├── ClinicServiceImpl.java
    │   │   │   ├── UserService.java
    │   │   │   └── UserServiceImpl.java
    │   │   └── repository/                 # 3 implementations: JDBC, JPA, Spring Data
    │   │       ├── OwnerRepository.java
    │   │       ├── PetRepository.java
    │   │       ├── PetTypeRepository.java
    │   │       ├── VetRepository.java
    │   │       ├── VisitRepository.java
    │   │       ├── SpecialtyRepository.java
    │   │       ├── UserRepository.java
    │   │       ├── jdbc/                   # JDBC implementations
    │   │       ├── jpa/                    # JPA implementations
    │   │       └── springdatajpa/          # Spring Data JPA implementations
    │   └── resources/
    │       ├── application.properties
    │       ├── openapi.yml                 # OpenAPI spec (DTOs generated from this)
    │       └── db/
    │           ├── hsqldb/
    │           ├── mysql/
    │           └── postgresql/
    └── test/
        ├── java/org/springframework/samples/petclinic/
        │   ├── rest/
        │   ├── service/
        │   └── mapper/
        └── resources/
            └── application-test.properties
```

## 🚀 Quick Start

### Prerequisites
- Java 21 (LTS)
- VS Code with GitHub Copilot extension
- Git

### Setup
```bash
# Clone the project
git clone <repo-url>
cd agentic-spring-boot-starter

# Build and test
./mvnw clean test

# Run the application
./mvnw spring-boot:run

# Test the API (port 9966, context path /petclinic/)
curl http://localhost:9966/petclinic/api/owners
curl http://localhost:9966/petclinic/api/pets
curl http://localhost:9966/petclinic/api/vets
curl http://localhost:9966/petclinic/api/visits
curl http://localhost:9966/petclinic/api/pettypes
curl http://localhost:9966/petclinic/api/specialties
```

### Using Copilot Agent Mode
1. Open the project in VS Code
2. Open Copilot Chat (Ctrl+Shift+I)
3. Switch to **Agent Mode** in the chat dropdown
4. Choose a chat mode:
   - **Spring Dev** — general development
   - **Test Mode** — focused testing
   - **Review Mode** — read-only code review
   - **Full Auto** — maximum autonomy for feature implementation
5. Use slash commands from the prompts: `/implement-feature`, `/new-rest-endpoint`, `/write-tests`, `/security-review`

### OpenAPI-First Architecture

This project follows an **OpenAPI-first** workflow:
- DTOs are generated from `src/main/resources/openapi.yml`
- **MapStruct** mappers handle entity-to-DTO conversion (no manual mapping code)
- The repository layer supports 3 interchangeable implementations: JDBC, JPA, and Spring Data JPA (selected via Spring profiles)

### Implementing a Feature Autonomously
The killer use case — give the agent a feature spec, it builds everything:

1. Switch to **Full Auto** chat mode
2. Use the `/implement-feature` prompt
3. Describe your feature using the OpenAPI-first workflow:
   ```
   /implement-feature

   Add a "MedicalRecord" feature for pets with these fields:
   - pet (required, reference to existing Pet)
   - recordDate (required, date)
   - diagnosis (required, max 255 chars)
   - treatment (optional, max 500 chars)

   Follow the OpenAPI-first workflow:
   1. Define the DTO schema in openapi.yml
   2. Create the JPA entity extending BaseEntity
   3. Create the MapStruct mapper
   4. Add methods to ClinicService
   5. Create the REST controller
   6. Include CRUD with search by pet.
   ```
4. The agent will autonomously:
   - Update openapi.yml with new DTO schemas
   - Create Entity extending the proper base class
   - Create MapStruct mapper for entity/DTO conversion
   - Add service methods to ClinicService
   - Create REST controller
   - Write unit + integration tests
   - Run `./mvnw test` to verify
   - Report what it built

## 🛡️ FSI Compliance Features

| Feature | Implementation |
|---------|---------------|
| No hardcoded secrets | Environment variables via `${VAR}` in application.yml |
| Input validation | Bean Validation (`@Valid`, `@NotBlank`, `@Size`) |
| Safe error responses | `@ControllerAdvice` — no stack traces exposed |
| Audit logging | SLF4J structured logging |
| Dependency security | Maven dependency tree analysis |
| Code review | Security Reviewer agent + review chatmode |

## 🔄 The Agentic Workflow

```mermaid
sequenceDiagram
    participant Dev as Developer
    participant VSC as VS Code
    participant GHC as GitHub Copilot
    participant Agent as Copilot Agent
    participant CLI as Terminal/CLI
    participant Code as Codebase

    Dev->>VSC: Opens project
    VSC->>GHC: Loads AGENTS.md + Instructions
    Dev->>GHC: "Create a new MedicalRecord feature"
    GHC->>Agent: Activates Spring Boot Developer agent
    Agent->>Code: Reads existing PetClinic code structure
    Agent->>Code: Defines DTO in openapi.yml
    Agent->>Code: Creates entity, MapStruct mapper, ClinicService methods, REST controller
    Agent->>Code: Creates unit + integration tests
    Agent->>CLI: ./mvnw test
    CLI-->>Agent: ✅ All tests pass
    Agent->>CLI: git diff
    CLI-->>Agent: Shows changes
    Agent-->>Dev: "Created MedicalRecord CRUD: openapi.yml, entity, mapper, service, controller + tests. All tests pass."
    Dev->>GHC: Switch to Review Mode
    Dev->>GHC: "Review the MedicalRecord endpoint"
    GHC->>Agent: Activates Security Reviewer agent
    Agent->>Code: Scans for security issues
    Agent-->>Dev: "No critical findings. 1 suggestion: add rate limiting."
```

## 📖 VS Code Settings Highlights

The `.vscode/settings.json` is pre-configured for maximum agentic productivity:

| Setting | Purpose |
|---------|---------|
| `chat.agent.enabled` | Enables Agent Mode |
| `chat.tools.terminal.allowlist` | Auto-approves safe Maven/Git/Java commands |
| `chat.tools.terminal.denylist` | Blocks dangerous commands (rm -rf, sudo) |
| `chat.instructionsFilesLocations` | Points to `.github/instructions/` |
| `chat.promptFilesLocations` | Points to `.github/prompts/` |
| `chat.modeFilesLocations` | Points to `.github/chatmodes/` |
| `github.copilot.chat.codeGeneration.useInstructionFiles` | Enables instruction files |

## 🤖 Full Autonomous Feature Implementation

The key innovation: the agent can implement a **complete feature from a spec** without human intervention.

### What Makes This Possible (Without MCP)

```mermaid
graph TB
    subgraph "Autonomy Enablers"
        A["autoApprove edits"] & B["terminal allowlist"] & C["autoFix"] & D["runTasks"]
    end

    A & B & C & D --> E["🤖 Autonomous Pipeline"]
    E --> F["openapi.yml · Entity · MapStruct Mapper · ClinicService · Controller · Tests"]
    F --> J["./mvnw test"]
    J -->|"✅ Pass"| K["Done"]
    J -->|"❌ Fail"| L["Auto-fix & Retry"] --> J

    style E fill:#27ae60,color:#fff
    style K fill:#27ae60,color:#fff
    style L fill:#e67e22,color:#fff
```

### Key Settings That Enable Autonomy

| Setting | Value | What It Does |
|---------|-------|-------------|
| `chat.tools.edits.autoApprove` | `true` | Agent creates/edits files without dialog |
| `chat.agent.autoFix` | `true` | Agent self-corrects compilation errors |
| `chat.agent.runTasks` | `true` | Agent runs VS Code build tasks |
| `chat.agent.maxRequests` | `50` | Allows complex multi-step implementations |
| `terminal.allowlist` | (regex) | Auto-approves `./mvnw`, `git`, `curl` |
| `terminal.denylist` | (patterns) | Blocks dangerous commands |
| `chat.tools.autoApprove` | `false` | ⚠️ Keep false for FSI safety |

### The Pattern The Agent Follows
The `implement-feature` prompt + `feature-pipeline` instruction file teach the agent to follow an OpenAPI-first pipeline: define the DTO in `openapi.yml`, create the JPA entity (extending `BaseEntity`/`NamedEntity`/`Person`), add a MapStruct mapper, implement `ClinicService` methods, and build the REST controller. The agent learns by reading the existing codebase patterns (e.g., `Owner` for a complex entity with relationships, `PetType` for a simple named entity, `Visit` for a dependent entity). This is critical: **the agent learns by reading your existing code patterns, not just instructions**.

## 📊 New: Diagramming, Orchestration & Quality Gates

Five new capabilities inspired by [petender/tdd-azd-demo-builder](https://github.com/petender/tdd-azd-demo-builder), adapted for Spring Boot / Java 21.

### 1. Diagrammer Agent — Architecture & ERD Diagrams

Automatically generates SVG architecture diagrams and JPA Entity-Relationship Diagrams by scanning your source code.

**How to use:**
```
# In VS Code Copilot Chat, select the "A24 Diagrammer" agent, then:
Generate an architecture diagram for this project

# Or use the prompts:
/generate-erd
/generate-architecture-diagram
```

**What it does:**
- Reads `pom.xml`, `application.yml`, controllers, services, repositories
- Generates Mermaid (.mmd) diagram definitions
- Renders them to SVG images in `docs/diagrams/`
- For ERDs: scans `@Entity` classes, extracts fields, relationships, and annotations

**Prerequisites** (installed automatically by the agent):
```bash
npm install -g @mermaid-js/mermaid-cli
```

**Output:**
| File | Content |
|---|---|
| `docs/diagrams/architecture.mmd` | Architecture diagram source (Mermaid) |
| `docs/diagrams/architecture.svg` | Rendered architecture diagram |
| `docs/diagrams/erd.mmd` | ERD source (Mermaid) |
| `docs/diagrams/erd.svg` | Rendered Entity-Relationship Diagram |
| `docs/diagrams/class-diagram.mmd` | Class diagram source (Mermaid) |
| `docs/diagrams/class-diagram.svg` | Rendered class diagram |

### 2. Research-First Instruction — Think Before You Code

A global instruction (applied to `**`) that forces ALL agents to research before implementing.

**How it works:**
- Automatically injected into every agent's context
- Requires agents to read existing code, patterns, and conventions before creating files
- Enforces an 80% confidence gate — agents must understand what to build before building it

**No action needed** — this is auto-injected. It improves output quality across all agents.

### 3. Execution Lead — End-to-End Feature Pipeline

An orchestrator agent that builds complete features by delegating to specialized agents in sequence.

**How to use:**
```
# In VS Code Copilot Chat, select "A24 Execution Lead", then describe your feature:

Create a "Visit" feature with fields:
- pet (required, reference to existing Pet)
- visitDate (required, date)
- description (required, max 255 chars)
- vet (optional, reference to existing Vet)
Include CRUD with search by pet and by date range.
```

**The pipeline:**
```
Step 1: Requirements    → Execution Lead parses your description
Step 2: API Design      → Delegates to API Designer
Step 3: Implement       → Delegates to Spring Boot Developer
Step 4: Test            → Delegates to Test Engineer
Step 5: Security Review → Delegates to Security Reviewer
Step 6: Diagram         → Delegates to Diagrammer (optional)
```

**Each step produces artifacts the next step reads** — agents communicate through files, not messages.

### 4. JPA ERD Generator — Auto-Document Your Data Model

A dedicated prompt that scans all `@Entity` classes and generates a professional ERD.

**How to use:**
```
/generate-erd
```

**What it extracts from your code:**
| JPA Annotation | ERD Element |
|---|---|
| `@Id` | Primary Key 🔑 |
| `@ManyToOne` | Foreign Key 🔗 + N:1 arrow |
| `@OneToMany` | 1:N relationship arrow |
| `@ManyToMany` | N:M relationship via junction |
| `@Column(nullable=false)` | NOT NULL constraint |
| `@Enumerated` | ENUM type |

### 5. Quality Gates — Automated Enforcement

An instruction (applied to `**/*.java`) that enforces minimum quality standards.

**Gates enforced:**

| Gate | Requirement |
|---|---|
| **Test** | All tests pass, minimum 3 per service + 3 per controller |
| **Build** | Zero compilation errors |
| **Security** | No hardcoded secrets, `@Valid` on all request bodies, no `System.out` |
| **Naming** | Test methods follow `should{Expected}When{Condition}` |
| **Git** | No secrets in diff, conventional commit messages |

**No action needed** — auto-injected for all `.java` files. Agents are blocked from reporting success if any gate fails.

### How These 5 Features Work Together

```mermaid
graph TB
    RF["🔍 Research-First"] & QG["✅ Quality Gates"]
    EL["🎯 Execution Lead"]

    RF & QG -.->|auto-injected| EL
    EL --> API["API Designer"] --> DEV["Developer"]
    DEV --> TEST["Tester"] --> SEC["Reviewer"]
    SEC --> DIAG["📊 Diagrammer"]

    style EL fill:#3498db,color:#fff
    style DIAG fill:#27ae60,color:#fff
    style RF fill:#f39c12,color:#fff
    style QG fill:#e74c3c,color:#fff
```

## 🤝 Contributing

This is a starter template. Customize it for your team:

1. **Modify `AGENTS.md`** to match your project's conventions
2. **Add instructions** for your specific frameworks and libraries
3. **Create prompts** for your team's common tasks
4. **Define agents** for your team's roles
5. **Tune the terminal allowlist** for your build tools

## 📚 Further Reading

- [GitHub Blog: How to write a great AGENTS.md](https://github.blog/ai-and-ml/github-copilot/how-to-write-a-great-agents-md-lessons-from-over-2500-repositories/)
- [VS Code: Custom Agents](https://code.visualstudio.com/docs/copilot/customization/custom-agents)
- [VS Code: Custom Instructions](https://code.visualstudio.com/docs/copilot/customization/custom-instructions)
- [VS Code: Prompt Files](https://code.visualstudio.com/docs/copilot/customization/prompt-files)
- [Agentic DevOps Safe Mode for Secure Copilot Agents](https://arinco.com.au/blog/agentic-devops-safe-mode-a-practical-framework-for-secure-github-copilot-agents/)
- [FSI Agent Governance Framework](https://judeper.github.io/FSI-AgentGov/playbooks/control-implementations/1.1/portal-walkthrough/)

## 📄 License

Internal use — Swiss Re / FSI environments.
