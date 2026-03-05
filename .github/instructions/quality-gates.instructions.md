---
description: "Quality gates for test coverage and security review — agents must verify output meets thresholds before marking work complete"
applyTo: "**/*.java"
---

# Quality Gate Requirements

Agents MUST pass these quality gates before reporting work as complete.

## Test Quality Gate

After writing or modifying tests:

1. **Run all tests**: `./mvnw clean test`
2. **All tests MUST pass** — zero failures, zero errors
3. **Minimum test coverage per feature**:
   - Service class: at least 3 tests (happy path, error/not-found, edge case)
   - Controller class: at least 3 tests (success response, validation error, not-found)
4. **If tests fail**: fix immediately and re-run — do NOT report success with failures

### Test Naming Convention Check

Every test method must follow: `should{Expected}When{Condition}`

```
shouldReturnGreetingWhenExists        ✅
shouldThrow404WhenNotFound            ✅
testGetGreeting                        ❌ (no should/when)
```

## Build Quality Gate

After any code change:

1. **Compile clean**: `./mvnw clean compile` — zero errors, zero warnings
2. **No lint errors**: check `get_errors` for the modified files
3. **If compilation fails**: fix before proceeding — never skip

## Security Quality Gate

Before marking a feature complete, verify:

| Check                    | How to Verify                                  | Must Pass |
| ------------------------ | ---------------------------------------------- | --------- |
| No hardcoded secrets     | Grep for `password`, `secret`, `key` in source | Yes       |
| Input validation present | Every `@RequestBody` has `@Valid`              | Yes       |
| Error handling safe      | `@ControllerAdvice` exists, no stack traces    | Yes       |
| Parameterized queries    | No string concatenation in queries             | Yes       |
| No `System.out.println`  | Grep for `System.out` / `System.err`           | Yes       |

## Git Quality Gate

Before committing:

1. `git diff` — review all changes
2. No secrets in the diff
3. No unrelated changes included
4. Commit message follows conventional commits: `feat:`, `fix:`, `test:`, `docs:`

## Enforcement

If any quality gate fails:

- **Do NOT skip it**
- **Do NOT report success**
- Fix the issue, re-run the gate, and only proceed when it passes
