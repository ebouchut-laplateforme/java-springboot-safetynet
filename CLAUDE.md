# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.4.2 REST API application for SafetyNet, a system that manages information about persons, fire stations, and medical records. The project uses Java 17 and follows standard Spring Boot conventions with Maven as the build tool.

## Technology Stack

- **Java**: 17 (managed via SDKMAN - see `.sdkmanrc`)
- **Spring Boot**: 3.4.2
- **Build Tool**: Maven 3.9.11 (managed via SDKMAN)
- **Key Dependencies**:
  - Spring Web (RESTful services)
  - Spring Boot DevTools (hot reload)
  - Spring Boot Actuator (monitoring)
  - Lombok (boilerplate reduction)
  - Log4j2 (logging)
  - JUnit 5 (testing)
  - JaCoCo (code coverage)

## Project Structure

```
src/
├── main/
│   ├── java/com/ericbouchut/springboot/safetynet/
│   │   └── JavaSpringbootSafetynetApplication.java (main entry point)
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/ericbouchut/springboot/safetynet/
        └── JavaSpringbootSafetynetApplicationTests.java

docs/
├── data.json (source data with persons, firestations, medicalrecords)
├── safetynet-consigne.pdf (project requirements)
└── safetynet-specifications-tech_stack.pdf (specifications)
```

## Data Model

The application works with three main data entities (see `docs/data.json`):

1. **Persons**: firstName, lastName, address, city, zip, phone, email
2. **Fire Stations**: address, station number (mapping addresses to fire station numbers)
3. **Medical Records**: firstName, lastName, birthdate, medications[], allergies[]

## Common Development Commands

### Building and Running

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Package as JAR
./mvnw clean package
```

### Testing

```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=JavaSpringbootSafetynetApplicationTests

# Run a specific test method
./mvnw test -Dtest=JavaSpringbootSafetynetApplicationTests#contextLoads

# Run tests with coverage report (JaCoCo)
./mvnw clean test jacoco:report
# Coverage report will be in target/site/jacoco/index.html
```

### Code Quality

```bash
# Compile only (useful for quick syntax checks)
./mvnw compile

# Compile tests without running them
./mvnw test-compile
```

### DevTools

The project includes Spring Boot DevTools, which enables automatic restart when classpath files change. When running with `./mvnw spring-boot:run`, the application will automatically restart when you modify and recompile code.

## Environment Setup

This project uses SDKMAN for Java and Maven version management. The `.sdkmanrc` file specifies:
- Java 17.0.17-tem
- Maven 3.9.11

To activate the correct versions:
```bash
sdk env install  # First time only
sdk env          # Activate environment
```

## Lombok Configuration

The project uses Lombok to reduce boilerplate code. The Maven compiler plugin is configured with Lombok annotation processing. When creating model classes, you can use Lombok annotations like:
- `@Data` (getters, setters, toString, equals, hashCode)
- `@Builder` (builder pattern)
- `@NoArgsConstructor`, `@AllArgsConstructor`
- `@Slf4j` (logging)

## Spring Boot Actuator

The application includes Spring Boot Actuator for monitoring. After starting the app, endpoints are available at:
- Health check: `http://localhost:8080/actuator/health`
- Application info: `http://localhost:8080/actuator/info`

Additional actuator endpoints may need to be enabled in `application.properties`.

## Application Architecture

This is a standard Spring Boot REST API application. When implementing features:

1. **Controller Layer**: Create REST controllers in `controller/` package with `@RestController` and `@RequestMapping` annotations
2. **Service Layer**: Business logic in `service/` package with `@Service` annotation
3. **Repository Layer**: Data access in `repository/` package (will likely need to load from `docs/data.json`)
4. **Model Layer**: Entity/DTO classes in `model/` or `domain/` package (use Lombok)

## Testing Strategy

- Use JUnit 5 for unit tests
- Use `@SpringBootTest` for integration tests
- Use `@WebMvcTest` for controller layer tests
- Mock dependencies with `@MockBean` when needed
- Aim for test coverage tracked by JaCoCo

## Logging

The project uses Log4j2 via `spring-boot-starter-log4j2` (Logback is excluded). Configuration is in `src/main/resources/log4j2-spring.xml` with Spring profile support (dev/prod/default). Use SLF4J with Lombok's `@Slf4j` annotation for logging in classes.

Log files are written to `logs/` directory (already in `.gitignore`).

## Important Reminders for Claude Code

**Always verify current state before making recommendations:**
- Re-read configuration files (pom.xml, application.properties, etc.) before suggesting changes
- Don't assume files haven't changed since the start of the conversation
- Check what's already configured before advising what "needs to be done"
- The user may have made changes between your reads

**Project-specific notes:**
- `.gitignore` already includes `logs/` and `*.log`
- `pom.xml` already excludes Logback and includes `spring-boot-starter-log4j2`
- Log4j2 configuration uses `log4j2-spring.xml` (not `log4j2.xml`) for Spring profile support
