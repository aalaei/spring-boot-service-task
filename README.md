# Spring Boot Web Application with MongoDB, Redis (Interview Task)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://aalaei.github.io/spring-boot-service-task/javadoc/)
![Tests Workflow](https://github.com/aalaei/spring-boot-service-task/actions/workflows/maven.yml/badge.svg)
![Release Workflow](https://github.com/aalaei/spring-boot-service-task/actions/workflows/release.yml/badge.svg)

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
- [Java Client Application](#java-client-application)
- [Project Milestones and Progress](#project-milestones-and-progress)
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

3. Configure MongoDB and Redis connection details in `application.properties` or [`application.yml`](./src/main/resources/application.yml).

### Running the Application
This guide explains how to run the Java Spring Boot application in two ways: using Docker and locally.

#### Docker Compose
To run the application along with MongoDB and Redis using Docker Compose, execute the following command:

```bash
docker-compose up
```

This command will orchestrate the setup defined in the `docker-compose.yml` file, including building and running the Spring Boot application, MongoDB, and Redis within separate Docker containers. Access the application in your browser by navigating to http://localhost:3000.

### Run Locally
**Without Docker**
If you choose to run the Spring Boot application locally without Docker, note that you must set up Redis and MongoDB manually. Ensure that Redis and MongoDB are running on your local machine with the necessary configurations.
**With Docker**
Alternatively, you can leverage Docker Compose for local development as well. When running locally as a Docker application, the Spring Boot app will listen on a different port, avoiding conflicts with your local setup. This allows you to run both Dockerized MongoDB and Redis containers alongside your Spring Boot application without any issues.


Execute the following command to build and run the application:

```bash
./run.sh
```
This bash script handles the necessary steps for building, packaging, and running the Spring Boot application.
Access the application in your browser by navigating to http://localhost:8080.

## API Endpoints
The API provides the following endpoints for CRUD operations on the "Service" object:
* `POST /api/v1/services`: Create a new service, with the required service information provided in the request body.
* `GET /api/v1/services?id=<ID>`: Retrieve a service by ID or list of them.
* `GET /api/v1/services`: Retrieve a list of services(only Ids).
* `GET /api/v1/services/all`: Retrieve a list of services(All Details).
* `PUT /api/v1/services?id=<ID>`: Update an existing service by ID, with the required service information provided in the request body.
* `DELETE /api/v1/services?id=<ID>`: Delete a service by ID.

The API provides the following endpoints for User Management and Authentication:
* `POST /api/v1/auth/login`: Login and Return JWT token.
* `POST /api/v1/auth/register`: Create a new User by username and password.
* `GET /api/v1/users?username=<username>`: Retrieve a User or a list of them.
* `PUT /api/v1/services?username=<username>`: Update an existing user by username, with the required user information provided in the request body.
* `DELETE /api/v1/users?username=<username>`: Delete the specified user.

The API provides the following endpoints for actuator:
* `GET /actuator/health`: Check the application's health status.
* `GET /actuator/info`: Get into of the application.

The API provides the following paths for API documentation and Swagger UI:
* `/swagger-ui.html`: Swagger UI
* `/v3/api-docs`: API Documentation JSON

Refer to the API documentation or Swagger UI for detailed information on request and response structures.

## Spring Profiles
The application supports two Spring profiles: `dev` and `prod`. Configure profile-specific properties in [`application-dev.yml`](./src/main/resources/application-dev.yml) and [`application-prod.yml`](./src/main/resources/application-prod.yml).

In addition to the properties specified in the above profiles, security properties can be set in `secrets.yml` as shown below(This is the default security config):

```yml
jwt-secret-key: 9aae401561d4d70a65529615cef26f3794da2ffdebe0304f846b1e72f79b64b7
admin-pass: admin
dto:
  encryption:
    enabled: true
    key: NGHJJjWm+gp/lmJ4lX3JOA==
    initVector: K869pc8rp6oSPQwJVGvM/Q==
    algo: "AES/CBC/PKCS5PADDING"
db:
  encryption:
    enabled: true
    key: rYUwouQ16kswUnNYkdNDig==
    initVector: 0srIh8CGYrPESYZxZO9v1A==
    algo: "AES/CBC/PKCS5PADDING"

```
Before making changes to this configuration, please consider the following:
* Ensure DTO security settings match between the client and server.
* Understand the need to clean the cache after modifying encryption configurations.
* Exercise caution when altering database encryption settings to avoid unintended data corruption.
* Keep the security configuration private, as the overall system security relies on it.


To activate a profile, set the `spring.profiles.active` property in [`application.yml`](./src/main/resources/application.yml) or choose the profile in [`pom.xml`](./pom.xml) file.

## Redis Caching
The application utilizes Redis caching for improved performance. New objects are cached, and reads check the cache first. The Caching configuration is in the `RedisCacheConfig` class.

## Optional Features
### Encryption Decryption
The server app employs two layers of encryption. One is at the database level, such as when utilizing a cloud provider for our database. This ensures that critical texts are encrypted, preventing unauthorized access by the provider. The second layer is in the connection between the client and the server, where critical text in Data Transfer Objects (DTOs) is encrypted. The `algo` parameter allows the selection of encryption algorithms, with the default set to `AES/CBC/PKCS5PADDING`. The encryption process utilizes the javax.crypto.Cipher class, supporting various encryption algorithms in Java. This default signifies the use of the AES cipher system in CBC mode with PKCS5PADDING padding. Specific text fields undergo encryption for added security, and you can find more details in the encryption service.

### GraphQL
This GraphQL API provides access to a system managing services, resources, and owners. It supports various queries and mutations to interact with the underlying data. The main entities in the system are Service, Resource, and Owner, each having specific attributes and relationships.

#### Queries
- servicesPaged(page: Int, size: Int): [Service]
- services: [Service]
- service(id: ID!): Service
- resources: [Resource]
- resource(id: ID!): Resource
- owners: [Owner]
- owner(id: ID!): Owner

#### Mutations
- createService(service: ServiceInput!): Service
- createResource(resource: ResourceInput!, serviceId: ID!): Resource
- createOwner(owner: OwnerInput!, resourceId: ID!): Owner
- updateService(id: ID!, service: ServiceInput!): Service
- updateResource(id: ID!, resource: ResourceInput!): Resource
- updateOwner(id: ID!, owner: OwnerInput!): Owner
- deleteService(id: ID!): Service
- deleteResource(id: ID!): Resource
- deleteOwner(id: ID!): Owner

#### Input Types
- ServiceInput
- ResourceInput
- OwnerInput

#### Object Types
* **Service**
    * id: ID!
    * criticalText: String
    * resources: [Resource]

* **Resource**
    * id: ID!
    * criticalText: String
    * owners: [Owner]

* **Owner**
    * id: ID!
    * criticalText: String
    * name: String
    * accountNumber: String
    * level: Int

## Git Version Control
This project uses Git for version control. Commits are structured to reflect development steps. Make sure to follow a similar convention in your contributions.

## Bash Script
The `run.sh` script simplifies the process of building and running the application. Ensure execute permissions are set:

```bash
chmod +x run.sh
```

# Java Client Application

An example Java client application is provided in the client directory of this repository. This client showcases how to interact with the API programmatically using Java. It authenticates using a username and password and holds a JWT token for authentication as a bearer token. It encrypts services and sends them to the server, retrieves encrypted information from the server, and then decrypts it. Any malicious entity cannot eavesdrop on critical texts. For more information, you can refer to the [Client Documentation](./client/README.md).

To run the Java client, Navigate to the client directory, then Build and run the Java client:

```bash
cd client
mvn clean package
java -jar target/clientApp.jar
```

This client application demonstrates various API calls and serves as a reference for integrating your project programmatically.

Feel free to explore and adapt the client code based on your specific use case.

# Project Milestones and Progress

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
- [x] Define relationships using annotations like `@DBRef`.

- [x] Create a `ServiceController` class.
- [x] Implement CRUD methods for the `Service` object.
- [x] Use `@RestController` and map endpoints for Create, Read, Update, and Delete operations.
- [x] Implement validation and exception handling.
- [x] Implement GraphQL API.
- [x] Authentication and Authorization using JWT.

- [x] Create two profiles, e.g., `dev` and `prod`.
- [x] Configure profile-specific properties in `application.properties` or [`application.yml`](./src/main/resources/application.yml).
- [x] Include database connection details for each profile.

- [x] Configure Redis caching by adding `@EnableCaching` to the main application class.
- [x] Use `@Cacheable` and `@CachePut` annotations in service methods.
- [x] Implement caching and updating data in Redis.

- [x] Implement encryption/decryption for critical text fields.

- [ ] Create scripts to dump MongoDB and Redis data locally.

- [x] Mapping between DTOs and entities.
- [x] Write unit tests for controllers, services, and repositories.
- [x] Document code using JavaDoc for better readability.
- [x] CI/CD Github workflow.
- [x] Dockerize Application.
- [x] API documentation and Swagger UI.
- [x] Implement Java Client App.

## License
This project is licensed under the MIT License - see the [LICENSE](https://opensource.org/licenses/MIT) file for details.
