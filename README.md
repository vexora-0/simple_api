# Simple API

## Overview

This project is a simple Spring Boot application that provides a RESTful API. It includes a single endpoint that returns a "Hello World!" message.

## Local Setup

To set up the project locally, follow these steps:

* Ensure you have Java 17 installed on your machine.
* Clone the repository to your local machine.
* Navigate to the project directory.
* Run the following command to build the project:
  ```sh
  ./mvnw clean install
  ```
* Start the application by running:
  ```sh
  ./mvnw spring-boot:run
  ```
* The application will be accessible at `http://localhost:8080`.

## Endpoint

* `GET /hello` - Returns a "Hello World!" message.
