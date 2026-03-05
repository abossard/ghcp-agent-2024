---
description: "Mandatory research-before-implementation requirements for all agents"
applyTo: "**"
---

# Research-First Requirement

**MANDATORY: Perform research before implementation.**

## Before Creating ANY Output

1. **Read existing code** — scan for patterns, conventions, reference implementations
2. **Read relevant skills** — load the SKILL.md for your domain
3. **Validate inputs** — confirm all required context exists
4. **Achieve 80% confidence** — only proceed when you understand what to build, where, and how

## Research Workflow

### Step 1: Context Gathering (REQUIRED)

Use read-only tools first — do not create or edit files yet:

- Search workspace for related code and patterns
- Read existing implementations as reference (e.g., `Greeting` feature)
- Read `pom.xml` for dependencies and project config
- Read `application.yml` for runtime configuration

### Step 2: Validation Gate (REQUIRED)

Before implementation, confirm:

1. Required inputs exist (previous artifacts, specs, context)
2. Existing patterns understood (how does the project already do this?)
3. Standards reviewed (naming, structure, testing conventions)

### Step 3: Confidence Check

If confidence is below 80%:

- Run additional searches
- Read more source files
- Apply documented defaults rather than guessing

**Only then proceed to implementation.**

## DO

- Research BEFORE creating files
- Read existing code BEFORE writing new code
- Check existing patterns BEFORE creating new ones
- Validate inputs BEFORE proceeding

## DON'T

- Skip research to "save time"
- Assume requirements without reading context
- Create output without understanding existing conventions
- Make up information when uncertain
