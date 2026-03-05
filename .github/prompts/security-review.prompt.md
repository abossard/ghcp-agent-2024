---
description: "Perform a security review of the codebase following OWASP and FSI standards"
agent: agent
tools:
  - codebase
  - search
---

# Security Review (FSI)

Perform a thorough security review of this Spring Boot application, focusing on FSI (Financial Services Industry) requirements.

## Checklist

### Secrets & Configuration
- [ ] No hardcoded passwords, tokens, or API keys in source
- [ ] Secrets loaded from environment variables or config server
- [ ] No sensitive data in `application.yml` committed to git
- [ ] `.gitignore` excludes sensitive files

### Input Validation
- [ ] All `@RequestBody` params have `@Valid`
- [ ] Bean Validation annotations on all DTOs/entities
- [ ] No raw SQL concatenation
- [ ] URL parameters validated and sanitized

### Error Handling
- [ ] No stack traces in API responses
- [ ] Global `@ControllerAdvice` for exception handling
- [ ] Generic error messages for 500 errors
- [ ] Structured error response format

### Logging
- [ ] No PII or secrets logged
- [ ] Security events logged (auth failures, etc.)
- [ ] Appropriate log levels used

### Dependencies
- [ ] No known CVEs in dependencies (`./mvnw dependency:tree`)
- [ ] All dependencies justified and necessary
- [ ] Spring Security configured (if auth required)

### CORS & Headers
- [ ] CORS not overly permissive
- [ ] Security headers configured (CSP, X-Frame-Options, etc.)

## Output Format
List each finding with:
- **Severity**: Critical / High / Medium / Low
- **File**: Path to the affected file
- **Issue**: Description of the problem
- **Fix**: Recommended remediation
