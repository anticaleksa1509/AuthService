# Authentication and Authorization Service

This is a Spring Boot-based microservice designed to handle user authentication and authorization. It uses **JWT (JSON Web Token)** for secure and stateless communication.

## Features

- **User Authentication**: Verifies user credentials and generates JWT tokens for sessionless authentication.
- **User Authorization**: Ensures users can only access resources they are permitted to.
- **JWT Integration**: Encodes user details and permissions in a secure, stateless manner.
- **Role-based Access Control (RBAC)**: Supports different user roles with customizable permissions.
- **Secure Password Handling**: Passwords are stored using strong encryption (e.g., BCrypt).
- **REST API Endpoints**: Easy-to-use endpoints for login, registration, and authorization checks.

## Technologies Used

- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Hibernate/JPA**
- **MySQL** 
- **Maven** for project management
