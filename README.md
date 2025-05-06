# Backend API

This is a backend project built with:

- Kotlin
- Spring Boot
- PostgreSQL
- Spring Data JPA
- Spring Security (JWT)
- Flyway (database migrations)
- Gradle (Kotlin DSL)
- MapStruct (DTO mapping)
- OpenAPI/Swagger (API docs)

## Getting Started

1. Configure your database and environment variables in `src/main/resources/application.properties`.
2. Build the project:
   ```sh
   ./gradlew build
   ```
3. Run the project:
   ```sh
   ./gradlew bootRun
   ```
4. API docs available at `/swagger-ui.html` when running locally.

## Notes

- Default admin user is created on first run:  
  Email: `admin@example.com`  
  Password: `admin123`
