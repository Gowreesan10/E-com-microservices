# Order Service

Lightweight README for the Order Service microservice. This document explains how to run the service, common developer workflows, debugging tips, and test instructions for future development.

## Table of contents
- Overview
- Quickstart (Docker & Maven)
- Configuration
- Database
- Debugging
- Testing
- Developer notes & useful commands
- Troubleshooting

## Overview

`order_service` is a Spring Boot microservice that manages orders for the sample e-commerce platform. It expects a MySQL database (there's a local Docker MySQL folder and init scripts included under `docker/mysql`).

This README focuses on how to run, debug, and test the service locally and in Docker during development.

## Prerequisites

- JDK 11+ (project uses Maven; match the project's pom.xml Java version)
- Maven 3.6+ or the included wrapper (`mvnw` / `mvnw.cmd`)
- Docker & Docker Compose (for running the MySQL dependency)
- An IDE (IntelliJ IDEA, VS Code) for debugging and running tests

## Quickstart

1) Start dependent services (MySQL) with Docker Compose from the `order_service` folder:

```powershell
docker-compose up -d
```

2) Build and run the service with Maven (from `order_service`):

```powershell
# build
mvnw.cmd -f .\pom.xml clean package -DskipTests

# run
mvnw.cmd -f .\pom.xml spring-boot:run
```

Alternatively run from your IDE. The Spring Boot application entry is under `src/main/java` (look for the class annotated with `@SpringBootApplication`).

## Configuration

- Application config lives in `src/main/resources` and in packaged `target/classes/application.yaml` when built.
- Common overrides:
  - `spring.datasource.*` to point to a different database
  - `server.port` to change the HTTP port

Use environment variables or `-Dspring.profiles.active=dev` to switch profiles when supported.

## Database

- The repository includes a MySQL data snapshot and `docker/mysql/init.sql` used by the Docker compose setup. By default the service expects the database created by that compose setup.
- If you need a fresh DB, stop the MySQL container, remove the persisted `mysql` data directory, then `docker-compose up` will recreate it and run `init.sql`.

## Debugging

- Remote debug with your IDE:
  - Run the app from Maven with remote debugging enabled. Example JVM arg for a debug port (add to `MVN_OPTS` or your run config):

```powershell
# Example: start the app with remote debug on port 5005
mvnw.cmd -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005" spring-boot:run
```

- Attach the IDE debugger to `localhost:5005`.
- Enable DEBUG logging for classes/packages by editing `application.yaml` or by passing `--logging.level.com.code10.ecom.order_service=DEBUG` on the command line.
- Typical issues:
  - Service can't connect to MySQL: check Docker Compose status and the `spring.datasource.url` in your active profile.
  - Port conflicts: ensure `server.port` doesn't collide with other services.

## Testing

- Unit tests:

```powershell
mvnw.cmd -f .\pom.xml test -Dtest=YourTestClass
```

- Run the whole test suite:

```powershell
mvnw.cmd -f .\pom.xml test
```

- Integration tests:
  - If integration tests depend on a running MySQL, start `docker-compose` first.
  - Use a dedicated test profile (for example `application-test.yaml`) if present and pass `-Dspring.profiles.active=test`.

- Manual API tests:
  - Use curl or Postman to exercise endpoints on `http://localhost:<server.port>` after the app is running.

## Developer notes & useful commands

- Build and package:

```powershell
mvnw.cmd -f .\pom.xml clean package
```

- Run only the application (skip tests):

```powershell
mvnw.cmd -f .\pom.xml spring-boot:run -DskipTests
```

- Show running Docker containers (Windows PowerShell):

```powershell
docker ps
```

- Stop and remove containers started by compose:

```powershell
docker-compose down -v
```

- Run a single test class (example):

```powershell
mvnw.cmd -f .\pom.xml -Dtest=com.code10.ecom.order_service.YourTestClass test
```

## Troubleshooting

- If the app fails to start with DB connection errors:
  - Ensure MySQL container is healthy (`docker ps` and `docker logs <mysql-container>`).
  - Check credentials in `application.yaml` or environment variables used by compose.

- If tests fail intermittently:
  - Confirm the test DB state and whether tests rely on ordering or leftover data.
  - Use `-DskipTests` to bypass while iterating on unrelated code.

## Contributing / Next steps

- Add or update `application-*.yaml` profiles for local dev and CI.
- Add integration-test Docker Compose target if you want tests to spin up fresh DBs automatically.

---

If you want, I can also:
- Add a `docker-compose.test.yml` for test-managed DBs.
- Add a `Makefile` or PowerShell helper script with the most-used commands.
