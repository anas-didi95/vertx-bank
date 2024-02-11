# Vert.X Bank System Microservices

[![Docker Image CI](https://github.com/anas-didi95/vertx-bank/actions/workflows/docker-image.yml/badge.svg?branch=main)](https://github.com/anas-didi95/vertx-bank/actions/workflows/docker-image.yml)

## Overview
This repository contains a set of microservices designed to support various functionalities of a bank system. The microservices are built to be modular, scalable, and independently deployable components that collectively form a robust banking system.

## Features
- **Account Management**: Handles creation, deletion, and modification of user accounts.
- **Transaction Service**: Facilitates transactions such as deposits, withdrawals, and transfers between accounts.
- **Authentication Service**: Provides authentication and authorization functionalities for users accessing the system.
- **Notification Service**: Sends notifications to users for important events such as transactions, account updates, etc.
- **Reporting Service**: Generates reports on user transactions, account summaries, and other financial data.
- **Security**: Implements robust security measures including encryption, token-based authentication, and secure communication protocols.
- **Scalability**: Designed to scale horizontally to handle increased loads by deploying multiple instances of each microservice.

## Technologies Used
- **Java**: The primary programming language for the project.
- **Maven**: Used as the build automation tool and dependency management.
- **Vert.x**: A toolkit for building reactive and distributed systems on the JVM. It's used for building high-performance, event-driven microservices.
- **JUnit 5**: A testing framework for Java programming language. It's used for unit testing in this project.
- **Apache Maven Compiler Plugin**: Maven plugin for compiling Java source code.
- **Apache Maven Shade Plugin**: Maven plugin for creating an executable jar with all dependencies included (fat jar).
- **Apache Maven Surefire Plugin**: Maven plugin for running unit tests.
- **Apache Maven Exec Plugin**: Maven plugin for executing Java programs.

## Getting Started

### Prerequisites
Before getting started, ensure you have the following prerequisites installed on your system:
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html): Make sure you have Java JDK 17 or later installed.
- [Apache Maven](https://maven.apache.org/download.cgi): Maven is required for building and managing dependencies. Install the latest version of Apache Maven.
- [Visual Studio Code](https://code.visualstudio.com/): Make sure you have VSCode installed on your system.
- [Visual Studio Code Remote - Containers Extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers): This extension allows you to develop inside a container using Visual Studio Code.

### Using Visual Studio Code DevContainer
This project is configured to be used with Visual Studio Code DevContainers, which provides a development environment isolated within a Docker container. Follow these steps to set up the project using DevContainers:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/anas-didi95/vertx-bank.git
   ```
2. **Open the Project in Visual Studio Code**:
   Navigate to the project directory and open it in Visual Studio Code:
   ```bash
   cd vertx-bank
   code .
   ```
3. **Reopen in Container**:
   Once the project is opened in VSCode, if you have the Remote - Containers extension installed, you should see a pop-up recommending to reopen the project in a DevContainer. Click on "Reopen in Container" to start the DevContainer setup.
4. **Build and Run the Project**:
   Once the DevContainer is set up, you can build and run the project directly from within VSCode. Open a terminal in VSCode and run:
   ```bash
   cd ms-bank-svc
   mvn package
   mvn exec:java
   ```
5. **Accessing the Application**:
   The application will be accessible at `http://localhost:8080`. You can test the endpoints using tools like cURL, Postman, or any web browser.
6. **Running Tests**:
   To run the unit tests for the project, execute the following command in the terminal:
   ```bash
   mvn test
   ```
7. **Development Environment**:
   For development, you can use VSCode with the DevContainer. It provides an isolated and consistent development environment across different machines. You can easily navigate the codebase, run/debug the application, and manage dependencies.

## Usage
- **API Documentation**: Detailed API documentation is available in the `misc` directory for each microservice.
- **Sample Requests**: Sample API requests and responses are provided to guide usage.
- **Authentication**: Ensure proper authentication and authorization tokens are used for accessing protected endpoints.

## Contant
Created by [Anas Juwaidi](mailto:anas.didi95@gmail.com)
