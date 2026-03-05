---
name: maven-build
description: >
  Maven build automation for Spring Boot projects. Handles compilation, packaging, 
  dependency management, and build troubleshooting. USE FOR: build errors, dependency 
  issues, Maven wrapper, packaging, compilation failures, dependency tree analysis.
---

# Maven Build Skill

## Procedure

### Build Commands (always use Maven Wrapper)
```bash
./mvnw clean compile                          # Clean and compile
./mvnw clean package -DskipTests              # Package without tests
./mvnw clean package                          # Full build with tests
./mvnw clean install                          # Install to local repo
./mvnw dependency:tree                        # Show full dependency tree
./mvnw dependency:tree -Dincludes=groupId     # Filter dependency tree
./mvnw versions:display-dependency-updates    # Check for updates
./mvnw help:effective-pom                     # Show resolved POM
```

### Troubleshooting Build Failures

1. **Compilation Error**: Read the error, find the file and line, fix the Java code
2. **Dependency Conflict**: Run `./mvnw dependency:tree` to find conflicts, use `<exclusions>` in pom.xml
3. **Maven Wrapper Missing**: Run `mvn wrapper:wrapper` to regenerate
4. **Out of Memory**: Set `MAVEN_OPTS="-Xmx1024m"` before running

### Adding Dependencies
When adding a dependency to `pom.xml`:
1. Add the `<dependency>` block in the appropriate section
2. Run `./mvnw compile` to verify resolution
3. Run `./mvnw test` to verify compatibility
4. Run `./mvnw dependency:tree` to check for conflicts

### Profile Management
```bash
./mvnw clean package -P production            # Activate production profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev  # Run with dev profile
```
