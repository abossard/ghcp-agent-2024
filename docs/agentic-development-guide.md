# Agentic Development Guide for Teams

> A practical guide for development teams adopting GitHub Copilot Agent Mode in restricted environments.

## What is Agentic Development?

Agentic development is a paradigm where AI coding agents work semi-autonomously alongside developers. Instead of manually typing every line of code, developers direct agents to:
- Create entire features (entities, services, controllers, tests)
- Run builds and tests
- Review code for quality and security
- Refactor existing code

The key shift is from **writing code** to **directing code creation**.

## How It Works in Your Environment

```
┌──────────────────────────────────────────────────────────┐
│                    VS Code + GitHub Copilot               │
│                                                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │
│  │  Chat Modes  │  │   Prompts   │  │   Agents    │      │
│  │  - Dev       │  │  - /new-api │  │  - Developer│      │
│  │  - Test      │  │  - /test    │  │  - Tester   │      │
│  │  - Review    │  │  - /review  │  │  - Reviewer │      │
│  └──────┬───────┘  └──────┬──────┘  └──────┬──────┘      │
│         │                 │                 │              │
│         └────────────┬────┘─────────────────┘              │
│                      ▼                                     │
│  ┌───────────────────────────────────────────────────┐    │
│  │              AGENTS.md + Instructions               │    │
│  │  - Tech stack, conventions, boundaries              │    │
│  │  - Java 21 style, Spring Boot patterns              │    │
│  │  - Security rules, testing requirements             │    │
│  └──────────────────────┬────────────────────────────┘    │
│                         ▼                                  │
│  ┌───────────────────────────────────────────────────┐    │
│  │                 Terminal Tools                       │    │
│  │  ./mvnw test | git diff | curl | java -version     │    │
│  │  (auto-approved via allowlist)                      │    │
│  └───────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────┘
```

## Getting Started (Step by Step)

### Step 1: Install VS Code Extensions
Open VS Code and install the recommended extensions:
```
Ctrl+Shift+P → "Extensions: Show Recommended Extensions"
```
All required extensions are listed in `.vscode/extensions.json`.

### Step 2: Understand the Configuration Files

| File | What It Does | When It's Used |
|------|-------------|----------------|
| `AGENTS.md` | Project-wide instructions for all agents | Every agent interaction |
| `.github/copilot-instructions.md` | Default Copilot behavior | Every Copilot suggestion |
| `.github/instructions/*.instructions.md` | File-specific rules | When editing matching files |
| `.github/prompts/*.prompt.md` | Reusable task templates | When you invoke `/command` |
| `.github/chatmodes/*.chatmode.md` | Chat personality presets | When you switch modes |
| `.github/skills/*/SKILL.md` | Multi-step procedures | When agent needs specialized knowledge |
| `.github/agents/*.md` | Custom agent personas | When you select an agent |

### Step 3: Choose Your Workflow

#### Quick Feature Development
1. Open Copilot Chat → Agent Mode → **Spring Dev** chatmode
2. Type: "Create a new REST endpoint for managing Users with CRUD operations"
3. Agent creates all files: entity, repo, service, controller, tests
4. Agent runs `./mvnw test` automatically
5. Review the changes with `git diff`

#### Test-First Development
1. Switch to **Test Mode** chatmode
2. Type: "Write comprehensive tests for UserService"
3. Agent analyzes the service, creates test file
4. Agent runs tests and reports results

#### Security Review
1. Switch to **Review Mode** chatmode
2. Type: "Review the User endpoint for security issues"
3. Agent scans code without modifying it
4. Reports findings with severity levels

### Step 4: Use Prompt Commands
In any chat mode, use slash commands for common tasks:
- `/new-rest-endpoint` — Full CRUD endpoint creation
- `/write-tests` — Test generation for a class
- `/security-review` — Security audit
- `/refactor-service` — Service refactoring
- `/add-entity` — New JPA entity
- `/explain-codebase` — Architecture overview

### Step 5: Iterate and Customize
- Edit `AGENTS.md` to match your team's conventions
- Add new prompts for your common tasks
- Create team-specific agents
- Tune the terminal allowlist for your tools

## Best Practices for Agentic Development

### DO ✅
- **Be specific in your requests** — "Create a User entity with name (required, max 100 chars), email (unique, validated), and createdAt timestamp"
- **Use prompts for repetitive tasks** — Create once, use everywhere
- **Review agent output** — Always check the diff before committing
- **Run tests frequently** — Trust but verify
- **Keep instructions up to date** — They're living documents

### DON'T ❌
- **Don't blindly accept all changes** — Review everything
- **Don't skip tests** — Agent-generated code needs verification too
- **Don't over-approve terminal commands** — Keep the allowlist tight
- **Don't share secrets in chat** — Copilot remembers context
- **Don't ignore security findings** — Especially in FSI environments

## Token Efficiency Tips

Since premium tokens may be limited:

1. **Use prompts** — Pre-defined tasks consume fewer tokens than free-form conversation
2. **Be concise** — Short, specific requests work better
3. **Use instructions** — They pre-load context without using chat tokens
4. **Batch work** — One comprehensive request is better than many small ones
5. **Use skills** — They provide procedural knowledge efficiently

## Measuring Success

Track these metrics to measure agentic development adoption:

| Metric | How to Measure |
|--------|---------------|
| Feature velocity | Time from request to working code |
| Test coverage | % of code covered by tests |
| Security findings | Issues found by review agent |
| Build stability | % of successful builds |
| Developer satisfaction | Survey on Copilot productivity |
