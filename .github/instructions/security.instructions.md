---
applyTo: "**/*.java"
---

# Security Instructions (FSI Environment)

## Secrets Management
- NEVER hardcode passwords, API keys, tokens, or connection strings
- Use environment variables: `${ENV_VAR:default_value}`
- Use `@ConfigurationProperties` with externalized configuration
- Add `secrets`, `password`, `key`, `token` patterns to .gitignore scanning

## Input Validation
- ALWAYS use `@Valid` on `@RequestBody` parameters
- Use Bean Validation annotations: `@NotNull`, `@NotBlank`, `@Size`, `@Pattern`, `@Min`, `@Max`
- Sanitize all user-supplied strings before logging
- Never trust client-side validation alone

## API Security
- Never expose stack traces in error responses
- Use generic error messages for 500 errors
- Return structured error objects with safe information only
- Configure CORS appropriately — never use `allowedOrigins("*")` in production
- Add rate limiting for public endpoints

## Data Access
- ALWAYS use parameterized queries (Spring Data JPA handles this automatically)
- Never concatenate user input into SQL strings
- Use `@Query` with named parameters: `@Query("SELECT u FROM User u WHERE u.email = :email")`
- Audit data access with Spring Data's `@CreatedBy`, `@LastModifiedBy`

## Logging
- NEVER log sensitive data: passwords, tokens, PII, credit card numbers
- Use MDC for request correlation
- Log security events: authentication, authorization failures, data access
- Mask sensitive fields in log output

## Dependencies
- Keep all dependencies up to date
- Run `./mvnw dependency:tree` to audit the dependency graph
- Monitor for CVEs in dependencies
- Only use approved dependencies from the internal artifact repository

## OWASP Top 10 Checklist
1. Broken Access Control — enforce authentication and authorization
2. Cryptographic Failures — use strong encryption, never roll your own
3. Injection — use parameterized queries, validate input
4. Insecure Design — follow secure design patterns
5. Security Misconfiguration — review Spring Security defaults
6. Vulnerable Components — keep dependencies updated
7. Authentication Failures — use strong password policies
8. Data Integrity Failures — validate all deserialized data
9. Logging Failures — log security events, protect log integrity
10. SSRF — validate and sanitize URLs before server-side requests
