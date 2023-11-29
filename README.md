# Spring Boot Web Application with MongoDB, Redis (Interview Taks)

![Tests Workflow](https://github.com/aalaei/spring-test/actions/workflows/maven.yml/badge.svg)
![Release Workflow](https://github.com/aalaei/spring-test/actions/workflows/release.yml/badge.svg)

This repository contains a Java Spring Boot web application that provides a REST API for managing a "Service" object with nested "Resource" and "Owner" objects. The application persists data in MongoDB, implements Redis caching, and includes optional features like encryption of critical text fields.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Spring Profiles](#spring-profiles)
- [Redis Caching](#redis-caching)
- [Optional Features](#optional-features)
  - [Encryption/Decryption](#encryption-decryption)
  - [GraphQL](#graphql)
- [Git Version Control](#git-version-control)
- [Bash Script](#bash-script)
- [TODO](#project-roadmap-todo-checklist)
- [License](#license)

## Prerequisites

Before running the application, make sure you have the following installed:

- Java JDK
- MongoDB
- Redis
- Git

## Getting Started

### Configuration

1. Clone the repository:

   ```bash
   git clone https://github.com/aalaei/spring-boot-service-task.git
   ```
2. Open the project in your preferred IDE.

3. Configure MongoDB and Redis connection details in `application.properties` or `application.yml`.

### Running the Application

- Execute the following command to build and run the application:

    ```bash
    ./run.sh
    ```
    This bash script handles the necessary steps for building, packaging, and running the Spring Boot application.

## API Endpoints
The API provides the following endpoints for CRUD operations on the "Service" object:
* `POST /api/v1/service`: Create a new service.
* `GET /api/v1/service/{id}`: Retrieve a service by ID.
* `GET /api/v1/service`: Retrieve a list of services.
* `PUT /api/v1/service/{id}`: Update an existing service.
* `DELETE /api/v1/service/{id}`: Delete a service by ID.
* `GET /health`: Check the health status of the application. 

Refer to the API documentation or Swagger UI for detailed information on request and response structures.

## Spring Profiles
The application supports two Spring profiles: `dev` and `test`. Configure profile-specific properties in `application-dev.yml` and `application-test.yml`.

To activate a profile, set the `spring.profiles.active` property in `application.yml` or choose the profile in `pom.xml` file.

## Redis Caching(To be implemented)
The application utilizes Redis caching for improved performance. New objects are cached, and reads check the cache first. The Caching configuration is in the `CacheConfig` class.

## Optional Features(To be implemented)
### Encryption Decryption
    Certain text fields are encrypted for added security. Refer to the encryption service for details.
### GraphQL

## Git Version Control
This project uses Git for version control. Commits are structured to reflect development steps. Make sure to follow a similar convention in your contributions.

## Bash Script
The `run.sh` script simplifies the process of building and running the application. Ensure execute permissions are set:

```bash
chmod +x run.sh
```

# Project Roadmap Todo Checklist

- [x] Initialize a Git repository in the project.
- [x] Commit regularly with clear commit messages.
- [x] Track development steps and changes.

- [x] Create a new Spring Boot project using [Spring Initializr](https://start.spring.io/).
- [x] Choose dependencies: Spring Web, Spring Data MongoDB, Spring Data Redis, and Lombok (optional).
- [x] Generate the project and import it into your preferred IDE.

- [x] Write a bash script for building, packaging, and running the application.
- [x] Provide clear instructions on setting up and running the application.
- [x] Include information on profiles, MongoDB and Redis setup, and other relevant details.

- [x] Create the `Service`, `Resource`, and `Owner` classes.
- [x] Use annotations like `@Document` for MongoDB entities.
- [ ] Define relationships using annotations like `@DBRef`.

- [x] Create a `ServiceController` class.
- [x] Implement CRUD methods for the `Service` object.
- [x] Use `@RestController` and map endpoints for Create, Read, Update, and Delete operations.
- [ ] Implement validation and exception handling.
- [ ] Implement GraphQL API.
- [ ] Authentication and Authorization using JWT.

- [x] Create two profiles, e.g., `dev` and `prod`.
- [x] Configure profile-specific properties in `application.properties` or `application.yml`.
- [x] Include database connection details for each profile.

- [ ] Configure Redis caching by adding `@EnableCaching` to the main application class.
- [ ] Use `@Cacheable` and `@CachePut` annotations in service methods.
- [ ] Implement caching and updating data in Redis.

- [ ] Implement encryption/decryption for critical text fields.

- [ ] Create scripts to dump MongoDB and Redis data locally.

- [ ] Mapping between DTOs and entities.
- [ ] Write unit tests for controllers, services, and repositories.
- [ ] Document code using JavaDoc for better readability.
- [x] CI/CD Github workflow.
- [x] Dockerize Application.
- [x] API documentation and Swagger UI.
- [ ] Implement Java Client App.

## License
This project is licensed under the MIT License - see the [LICENSE](https://opensource.org/licenses/MIT) file for details.
