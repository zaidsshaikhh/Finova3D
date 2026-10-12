# Finova 3D API

This project is a Spring Boot backend for a personal finance and expense management system.

## Features
- JWT authentication and authorization
- SQL Server + JPA/Hibernate
- Expense and income management
- Categories, budgets, goals, recurring transactions
- Dashboard analytics
- Spending insights
- Receipt upload
- Swagger UI

## Local setup

### Prerequisites
- Java 17+
- Maven 3.9+
- SQL Server 2022 or LocalDB

### Configure database
Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=Finova3D;encrypt=true;trustServerCertificate=true
    username: sa
    password: YourStrong!Passw0rd
```

### Run the app
```bash
mvn clean install
mvn spring-boot:run
```

### Swagger UI
Open:

```text
http://localhost:8080/swagger-ui.html
```

### Auth endpoints
- POST `/api/auth/register`
- POST `/api/auth/login`

### Example requests
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Aisha Khan",
    "email": "aisha@example.com",
    "password": "Test@1234"
  }'
```
