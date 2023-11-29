# Spring Boot Web Application with MongoDB, Redis(Interview Taks)

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

## License
This project is licensed under the MIT License - see the [LICENSE](https://opensource.org/licenses/MIT) file for details.
