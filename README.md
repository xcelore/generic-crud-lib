# xcelore-generic-crud-lib
A reusable **Spring Boot–based Generic CRUD service** built with **Java 17+** and **Maven**, providing standard Create, Read, Update, Delete, and Search functionality.

This project is designed to act as a **base CRUD library/service** that can be extended across multiple domain entities while ensuring consistent auditing, soft deletion, and clean request/response mapping.

---

## Features

- Generic CRUD operations
- Search and filtering support
- Request and response DTO mapping
- Base entity with audit fields
- Soft delete implementation
- PostgreSQL as default database
- Java 17+ compatible
- Maven-based build

---

## Tech Stack

- Java 17+
- Spring Boot
- Maven 3.5.5+
- PostgreSQL
- Spring Data JPA / Hibernate
- Lombok

---

## Project Structure

```
com.generic.service
├── controller     # REST controllers
├── service        # Business logic
├── repository     # JPA repositories
├── entity         # Base & domain entities
├── dto            # Request / Response models
├── listener       # Entity lifecycle listeners
└── config         # Configuration classes
```

---

## Generic Entity

All domain entities extend a common abstract base entity for consistency and auditing.

```java
@MappedSuperclass
@EntityListeners(GenericListener.class)
public abstract class GenericEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private UUID id;

    private UUID tenantId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UUID createdBy;
    private UUID updatedBy;

    @JsonIgnore
    private Boolean deleted = Boolean.FALSE;
}
```

---

## CRUD Operations

- Create
- Retrieve by ID
- Retrieve all (pagination supported)
- Update
- Soft delete
- Search and filtering

---

## Configuration

### PostgreSQL (Default)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/generic_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.5.5+
- PostgreSQL

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

---

## License

MIT License
