# Deep Research Notes — Agentic Development in FSI Environments

> Compiled research on unlocking GitHub Copilot Agent Mode for Spring Boot development in restricted FSI environments.

## Key Resources & Links

### Official Documentation
| Resource | URL | Notes |
|----------|-----|-------|
| AGENTS.md Specification | https://agents.md/ | Cross-tool agent instruction standard |
| GitHub: Writing Great AGENTS.md | https://github.blog/ai-and-ml/github-copilot/how-to-write-a-great-agents-md-lessons-from-over-2500-repositories/ | Lessons from 2,500+ repos |
| VS Code Custom Agents | https://code.visualstudio.com/docs/copilot/customization/custom-agents | Agent definition format |
| VS Code Custom Instructions | https://code.visualstudio.com/docs/copilot/customization/custom-instructions | .instructions.md format |
| VS Code Prompt Files | https://code.visualstudio.com/docs/copilot/customization/prompt-files | .prompt.md format |
| VS Code Agent Skills | https://code.visualstudio.com/docs/copilot/customization/agent-skills | SKILL.md format |
| Copilot Settings Reference | https://code.visualstudio.com/docs/copilot/reference/copilot-settings | All VS Code Copilot settings |
| Copilot Cheat Sheet | https://code.visualstudio.com/docs/copilot/reference/copilot-vscode-features | Feature reference |
| Custom Agents Config Reference | https://docs.github.com/en/copilot/reference/custom-agents-configuration | GitHub Docs |

### FSI & Enterprise Governance
| Resource | URL | Notes |
|----------|-----|-------|
| Agentic DevOps Safe Mode | https://arinco.com.au/blog/agentic-devops-safe-mode-a-practical-framework-for-secure-github-copilot-agents/ | Security framework for agent workflows |
| FSI Agent Governance Framework | https://judeper.github.io/FSI-AgentGov/playbooks/control-implementations/1.1/portal-walkthrough/ | RBAC, environment separation |
| Maximizing Copilot Agentic Capabilities | https://github.blog/ai-and-ml/github-copilot/how-to-maximize-github-copilots-agentic-capabilities/ | Productivity patterns |
| Enterprise Best Practices | https://markaicode.com/github-copilot-enterprise-best-practices-2025/ | 2025 best practices |
| End-to-End Agentic Development | https://xebia.com/articles/agentic-development/ | Xebia guide |
| Copilot for FSI | https://www.nasstar.com/insights/microsoft-copilot-for-the-financial-services-industry | FSI-specific |

### Community & Blog Posts
| Resource | URL | Notes |
|----------|-----|-------|
| Customizing Copilot Agent Mode | https://www.xkeshav.com/posts/github-copilot-agent-mode-tips/ | Practical tips |
| Custom Chat Modes | https://harrybin.de/posts/github-copilot-custom-chat-modes/ | Chatmode deep dive |
| Agent Skills Guide | https://smartscope.blog/en/generative-ai/github-copilot/github-copilot-skills-guide/ | Comprehensive skills guide |
| AGENTS.md Beyond VS Code | https://selim.github.io/2025/09/14/agents-md-in-vscode-and-beyond/ | Cross-tool compatibility |
| Build Your Own Agent | https://dxrf.com/blog/2025/11/20/build-your-own-github-copilot-agent/ | Step-by-step guide |
| Tuning Copilot Settings | https://dev.to/pwd9000/tune-github-copilot-settings-in-vs-code-32kp | Settings deep dive |
| Terminal Auto-Approve Guide | https://pratikpathak.com/fix-how-to-automatically-approve-continue-github-copilot-terminal-commands-in-vs-code/ | Allow/deny list patterns |

### GitHub Issues & Discussions
| Topic | URL | Notes |
|-------|-----|-------|
| Custom Agents Configuration | https://docs.github.com/en/copilot/reference/custom-agents-configuration | Format reference |
| Copilot Agent v1.104 Changelog | https://github.blog/changelog/2025-09-12-github-copilot-in-vs-code-august-release-v1-104/ | Terminal tool improvements |
| Instructions.md Support | https://github.blog/changelog/2025-07-23-github-copilot-coding-agent-now-supports-instructions-md-custom-instructions/ | Custom instructions support |
| Custom Agents Announcement | https://github.blog/changelog/2025-10-28-custom-agents-for-github-copilot/ | Feature launch |

## Key Findings

### 1. MCP Bypass Strategy
MCP is NOT required for agentic development. GitHub Copilot Agent Mode provides:
- **Custom Agents** (`.github/agents/`) — Role-based AI personas
- **Skills** (`.github/skills/`) — Multi-step procedural knowledge
- **Instructions** (`.github/instructions/`) — Context-specific coding rules
- **Prompts** (`.github/prompts/`) — Reusable task templates
- **Chat Modes** (`.github/chatmodes/`) — Workflow-optimized configurations
- **Terminal Tool Auto-Approve** — CLI command automation via allowlists

All of these work without MCP and are the recommended approach for restricted environments.

### 2. Terminal Tool Allowlist is the Key Enabler
The `github.copilot.chat.tools.terminal.allowlist` setting is the single most impactful configuration for agentic development:
- Allows Copilot to run Maven, Git, Java, and other CLI commands autonomously
- Regex-based patterns enable flexible matching
- Combined with a denylist, provides safe automation
- Eliminates the "click to approve" friction that kills agent flow

### 3. AGENTS.md is the "README for Agents"
- Place at project root for maximum visibility
- Include: tech stack, project structure, commands, coding conventions, boundaries
- Written for AI agents, not humans — be specific and actionable
- Cross-tool compatible (works with Claude Code, Cursor, and other tools)

### 4. FSI-Specific Considerations
- **Least privilege**: Restrict agent tool access to only what's needed
- **Human approval gates**: Use denylist to block dangerous commands
- **Audit trail**: Git commits provide full change tracking
- **No secrets**: Environment variables for all sensitive configuration
- **Code review**: Security Reviewer agent for automated security checks

### 5. Token Efficiency
With limited premium tokens:
- Keep instruction files concise and targeted
- Use path-scoped instructions (`applyTo`) to load only relevant context
- Prefer prompts over long conversational back-and-forth
- Use skills for complex multi-step workflows (loaded on-demand)

### 6. WSL2 Workaround
When WSL2 has Copilot extension incompatibilities:
- Use VS Code's Remote - WSL extension for file access
- Run Copilot in the Windows VS Code instance, not WSL
- CLI tools in WSL can still be accessed via integrated terminal
- Consider developing directly on Windows when possible

## Ideas for Future Enhancement

1. **Shared Skills Library**: Create an internal repository of `.github/skills/` that teams can copy
2. **CI/CD Integration Prompts**: Add prompts for GitHub Actions workflow creation
3. **Architecture Decision Records**: Add ADR prompt template
4. **Performance Testing Skill**: Add JMH benchmark skill
5. **Docker Skills**: Add containerization and Docker Compose skills
6. **OpenAPI Spec Skill**: Generate OpenAPI 3.0 specs from controllers
7. **Flyway Migration Skill**: Full database migration workflow
8. **Team-Specific Agents**: Create agents per team role (frontend, backend, QA, DevOps)
