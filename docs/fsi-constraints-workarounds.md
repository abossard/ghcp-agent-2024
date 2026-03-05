# FSI Constraints & Workarounds

> Specific guidance for overcoming restrictions in Financial Services Industry (FSI) environments when adopting agentic development with GitHub Copilot.

## The Constraint Map

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         FSI Environment                                  │
│                                                                          │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐         │
│  │ 🚫 MCP Blocked  │  │ 🚫 Open Code    │  │ ⚠️ Limited       │         │
│  │   by Policy     │  │   Not Allowed   │  │   Premium Tokens│         │
│  │                 │  │                 │  │                 │         │
│  │ Cannot use MCP  │  │ No Claude Code, │  │ Budget caps on  │         │
│  │ servers or      │  │ Cursor, Cody,   │  │ Copilot premium │         │
│  │ protocol        │  │ or other AI     │  │ model requests  │         │
│  │ extensions      │  │ coding tools    │  │                 │         │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘         │
│                                                                          │
│  ┌─────────────────┐  ┌─────────────────┐                               │
│  │ ⚠️ WSL2/Windows │  │ ⚠️ ADO MCP      │                               │
│  │   Incompatible  │  │   Dependency    │                               │
│  │                 │  │                 │                               │
│  │ Copilot exts    │  │ Azure DevOps    │                               │
│  │ may not work    │  │ MCP blocked     │                               │
│  │ in WSL2         │  │ with MCP policy │                               │
│  └─────────────────┘  └─────────────────┘                               │
└─────────────────────────────────────────────────────────────────────────┘
```

## Constraint-by-Constraint Solutions

### 🚫 Constraint 1: MCP is Blocked

**Impact**: Cannot use MCP servers for extended tool access (database queries, API calls, etc.)

**Solution**: Use built-in agent capabilities that DON'T require MCP:

| MCP Would Provide | Alternative Without MCP |
|-------------------|------------------------|
| Database queries | `./mvnw spring-boot:run` + `curl` against H2 console |
| API testing | `curl` commands via terminal tool |
| File search | Built-in `codebase` and `search` tools |
| Code execution | Terminal tool with allowlist |
| External docs | Skills with embedded documentation |

**VS Code Setting**:
```json
// Explicitly disable third-party tool auto-approval (MCP would use this)
"github.copilot.chat.agent.thirdParty.autoApprove": false
```

**Agent Configuration** (no mcp-servers):
```yaml
tools:
  - codebase
  - terminal
  - editFiles
  - search
  - usages
  - problems
# mcp-servers: {}  ← Do NOT configure any MCP servers
```

### 🚫 Constraint 2: Open Code Not Allowed

**Impact**: Cannot use Claude Code, Cursor, Cody, or other AI coding tools.

**Solution**: Maximize GitHub Copilot's built-in capabilities:
- Custom agents provide role-based behavior
- Skills provide domain-specific knowledge
- Instructions enforce coding standards
- Prompts automate common tasks
- Chat modes provide workflow optimization

**Key Insight**: AGENTS.md is a cross-tool standard. Even though you can only use Copilot today, your AGENTS.md will work if the tool restriction is lifted in the future.

### ⚠️ Constraint 3: Limited Premium Tokens

**Impact**: Cannot make unlimited requests to premium Copilot models.

**Solutions for Token Efficiency**:

1. **Pre-load context with instruction files**
   - Instructions are loaded automatically — no tokens spent explaining conventions
   - Use `applyTo` to scope instructions to relevant files only

2. **Use prompts instead of free-form chat**
   - Prompts encode the task specification once
   - Reusable across the team without re-explaining

3. **Use skills for complex workflows**
   - Skills provide procedural knowledge loaded on-demand
   - Only loaded when the task matches the skill description

4. **Batch requests**
   - "Create a User entity with repository, service, controller, and tests" (1 request)
   - NOT: "Create a User entity" → "Now add a repository" → "Now add a service" (3 requests)

5. **Use the right model**
   - Simple tasks: Use fast/cheap models
   - Complex tasks: Use premium models
   - Chat modes can specify the model to use

### ⚠️ Constraint 4: WSL2/Windows Incompatibility

**Impact**: Copilot extensions may not work correctly in WSL2 environment.

**Solutions**:

1. **Develop on Windows directly** (if possible)
   - Use Git Bash as terminal profile
   - Maven Wrapper (`mvnw.cmd`) works on Windows

2. **Use Remote - WSL extension** (partial solution)
   - Open files from WSL in Windows VS Code
   - Copilot runs in the Windows extension host
   - Terminal commands execute in WSL

3. **CLI-first approach** (best compatibility)
   - This project is designed to work entirely through CLI commands
   - `./mvnw` works in both Windows and WSL
   - Git commands work everywhere
   - `curl` for API testing works everywhere

**VS Code Setting** for Windows:
```json
"terminal.integrated.defaultProfile.windows": "Git Bash"
```

### ⚠️ Constraint 5: ADO MCP Blocked

**Impact**: Cannot use Azure DevOps MCP integration for work item management.

**Solutions**:
- Use Git branch naming conventions to link to work items: `feature/AB#12345-user-crud`
- Use commit message conventions: `feat: add user CRUD endpoint [AB#12345]`
- Use Azure DevOps CLI (`az boards`) via terminal if available
- Create a prompt template for ADO work item descriptions

## Security Governance Model

For FSI environments, implement this governance model:

```
Level 1: AGENTS.md
├── Defines what agents CAN do (tools, commands)
├── Defines what agents CANNOT do (boundaries)
└── Defines coding standards (security, quality)

Level 2: Terminal Denylist
├── Blocks dangerous commands (rm -rf, sudo, etc.)
├── Blocks secret exposure commands
└── Blocks system modification commands

Level 3: Terminal Allowlist
├── Only allows known-safe commands
├── Regex-based for flexibility
└── Regularly reviewed and updated

Level 4: Human Review
├── Git diff before commit
├── Code review on pull requests
├── Security scanning in CI/CD
└── Agent-generated code is NEVER auto-merged
```

## Recommended Governance Settings

```json
{
    // Level 1: Agent tools restriction
    // Only allow built-in tools — no MCP, no third-party
    "github.copilot.chat.agent.thirdParty.autoApprove": false,

    // Level 2: Terminal denylist (always active)
    "github.copilot.chat.tools.terminal.denylist": [
        "rm -rf /", "rm -rf ~", "sudo", "format", "del /",
        "rmdir /s", "mkfs", "dd if=", "chmod -R 777",
        "wget.*\\|.*sh", "curl.*\\|.*sh", "env", "printenv",
        "set | grep -i pass", "echo \\$.*PASSWORD"
    ],

    // Level 3: Terminal allowlist (only approved commands)
    "github.copilot.chat.tools.terminal.allowlist": [
        "^\\./mvnw ",  "^mvn ", "^java ",  "^git ",
        "^curl ", "^ls ", "^pwd", "^cat ", "^head ", "^tail ",
        "^find ", "^grep ", "^wc ", "^tree "
    ],

    // Level 4: No auto-approve (require human confirmation)
    "github.copilot.chat.tools.terminal.autoApprove": false
    // Note: allowlist still provides approve suggestions; human clicks to confirm
}
```

## Compliance Checklist

Before deploying this configuration in your FSI environment, verify:

- [ ] MCP settings are empty/disabled
- [ ] Terminal denylist blocks all dangerous commands
- [ ] Terminal allowlist only includes approved commands
- [ ] No third-party tool auto-approval
- [ ] AGENTS.md boundaries section covers all restrictions
- [ ] Security instruction file covers OWASP Top 10
- [ ] .gitignore excludes sensitive files
- [ ] No secrets in committed files
- [ ] CI/CD pipeline includes security scanning
- [ ] Code review process is documented
- [ ] Team has been trained on agent workflows

## Escalation Path

If an agent produces unexpected behavior:
1. **Stop**: Click cancel on any pending terminal command
2. **Review**: Check `git diff` for unintended changes
3. **Revert**: `git checkout -- .` to undo all changes
4. **Report**: Document the issue and share with the team
5. **Adjust**: Update AGENTS.md or settings to prevent recurrence
