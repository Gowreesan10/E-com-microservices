# E-Com Microservices

A Java 17 Spring Boot multi-module e‑commerce microservices demo providing product catalog, inventory, orders and notifications with an API Gateway and Docker-based local development.

## Modules
- apigateway — Spring Cloud Gateway + OAuth2 resource-server (edge routing & auth)
- product-service — product catalog (MongoDB)
- inventory-service — inventory management (MySQL + Flyway)
- order_service — order processing (MySQL)
- notification-service — notifications
- (root) pom.xml — parent multi-module Maven POM
- docker-compose.yml — root compose for local environment
- start-services.bat — helper for Windows

## Stack / notable libs
- Java 17
- Spring Boot 3.5.x, Spring Cloud (2025.0.0)
- Spring Cloud Gateway, Spring Security OAuth2 resource server
- Spring Data MongoDB, Spring Data JPA
- Flyway (DB migrations), Lombok, Resilience4j
- Springdoc OpenAPI (Swagger), Testcontainers, REST-assured

---

## Quick start (local)

Prereqs:
- JDK 17+
- Maven
- Docker & Docker Compose (v2)
- (Optional) Docker Desktop

1. Clone
```bash
git clone https://github.com/Gowreesan10/E-com-microservices.git
cd E-com-microservices
```

2. Build all modules (fast parallel build)
```bash
mvn -T 1C -DskipTests package
```

3. Start the whole stack
```bash
docker compose up --build
# or run detached:
docker compose up -d --build
```

4. Stop
```bash
docker compose down
```

Notes:
- Each service also contains a service-level `docker-compose.yml` for focused development (e.g., `product-service/docker-compose.yml`).
- Check the compose file(s) for the actual public ports — the gateway often runs on a single known port (commonly 8080) but confirm in your compose.

---

## Run a single service during development

From the repo root you can start one module without Docker:

Run the product service:
```bash
mvn -pl product-service -am spring-boot:run
```

Package and run the jar:
```bash
mvn -pl product-service -am -DskipTests package
java -jar product-service/target/*.jar
```

Replace `product-service` with any module name (`inventory-service`, `order_service`, `apigateway`, `notification-service`).

---

## Configuration & environment variables

Each service reads Spring configuration (application.yml/properties) and commonly-used env vars. Typical variables to set or review:

product-service (MongoDB)
- SPRING_DATA_MONGODB_URI
- SPRING_DATA_MONGODB_DATABASE

inventory-service (MySQL / JPA)
- SPRING_DATASOURCE_URL (jdbc:mysql://...)
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- FLYWAY_* (if overriding flyway config)

order_service (MySQL / JPA)
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD

apigateway (auth)
- JWT / OAuth2 configuration (issuer URI, jwk set URI or shared secret)
- SPRING_SECURITY_OAUTH2_RESOURCE_SERVER_JWK_SET_URI or similar

General
- Check each module’s `src/main/resources/application*.yml` and the module `docker-compose.yml` for exact keys used. Use `.env` or CI secrets to provide sensitive values (DB passwords, JWT secrets).

---

## Databases & initialization

- product-service: uses MongoDB — `product-service/mongo.sh` helps initialize sample data locally.
- inventory-service / order_service: include `localdb.sql` and Flyway is declared as a dependency in the modules (check `inventory-service` for migration scripts under `src/main/resources/db/migration` if present).

When using Docker Compose the compose files typically declare DB containers and populate them using the SQL/Mongo initialization scripts.

---

## API docs & exploration

OpenAPI / Swagger support is included via springdoc (parent pom contains springdoc dependencies). Each service should expose:
- `GET /v3/api-docs` (OpenAPI JSON)
- `GET /swagger-ui/index.html` or similar for interactive docs

Check each service after startup for its Swagger UI URL (or use the API gateway’s docs if the gateway aggregates docs).

---

## Tests

- Unit and integration tests use JUnit 5 and Testcontainers (MongoDB, MySQL, Kafka testcontainers are present in the pom).
- Run module tests:
```bash
mvn -pl product-service test
```
- Run all tests (slow):
```bash
mvn test
```

---

## Useful commands

- Rebuild everything:
```bash
mvn -T 1C -DskipTests package
docker compose up --build
```

- Tail logs for a specific service:
```bash
docker compose logs -f product-service
```

- Run a single module with hot reload:
```bash
mvn -pl product-service -am spring-boot:run
# edit code; devtools may auto-restart
```

---

## Contributing
- Fork -> branch `feature/your-feature` -> open PR to `main`.
- Add tests for new behavior.
- Document new env vars and API contract changes (update OpenAPI spec).
- Keep API contract stable; if breaking changes are required, bump service version and note in changelog.

---

## Troubleshooting

- Ports collision: confirm ports in `docker-compose.yml` and free conflicting ports.
- DB migration errors: inspect Flyway output in service logs; check `src/main/resources/db/migration` for SQL ordering.
- Auth / JWT issues: ensure gateway and services share the expected JWKS/JWT_SECRET configuration.
- If a service fails to start, run it locally (mvn spring-boot:run) to get immediate stack traces.

---

## Next steps & customization
This README is aligned to the repository layout and dependencies. I can:
- Add exact ports, example curl requests and sample API responses by extracting port mappings and endpoints from the repo's `docker-compose.yml` and the controllers.
- Create `.env.example` and step-by-step “first run” scripts.
Tell me whether you want me to:
1. Auto-extract ports and produce concrete curl examples, or
2. Add an `.env.example` and sample Docker Compose override for local dev.

---

## License
This project contains a LICENSE file in the repository root. Follow the terms in that file.
