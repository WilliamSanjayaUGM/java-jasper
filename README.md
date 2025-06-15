Java Jasper Deployment & Setup Guide

This guide explains how to run, build, test, and deploy the java-jasper Spring Boot + Vaadin + JasperReports application.

🚀 1. Running Locally

Requirements

Java 21

Maven 3.9+

Steps

# Clone the repository
git clone git@github.com:WilliamSanjayaUGM/java-jasper.git
cd java-jasper

# Run the application
mvn clean spring-boot:run

Access

UI: http://localhost:8080

H2 Console: http://localhost:8080/h2-console

💡 To change configurations, edit src/main/resources/application.properties.Example: Change the port by setting server.port=8081

🐳 2. Docker Deployment (Non-Prod & Prod)

2.1 Files Included

Dockerfile – Main build config

docker-compose.yml – Base setup

docker-compose.override.yml – Local development config

docker-compose.prod.yml – Production config

2.2 Running with Docker Compose

✅ Local Development

Example docker-compose.override.yml:

version: '3.8'

services:
  java-jasper:
    environment:
      SPRING_DATASOURCE_URL: jdbc:h2:mem:manulifedb
      SPRING_DATASOURCE_DRIVER_CLASS_NAME: org.h2.Driver
      SPRING_DATASOURCE_USERNAME: sa
      SPRING_DATASOURCE_PASSWORD:
      SPRING_H2_CONSOLE_ENABLED: true
      SPRING_H2_CONSOLE_PATH: /h2-console
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    volumes:
      - .:/app

Run locally:

docker-compose up --build

🚀 Production Environment

Update docker-compose.prod.yml:

Replace image: your-registry/java-jasper:prod with your image tag

Set SPRING_DATASOURCE_* environment variables according to your production database

Run in production:

docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

☁️ 3. Deploying to Non-Prod & Prod

Build & Push Docker Image

docker build -t java-jasper:latest .
docker tag java-jasper:latest <your-registry>/java-jasper:non-prod
docker push <your-registry>/java-jasper:non-prod

Pull & Run on Remote Server

docker pull <your-registry>/java-jasper:non-prod
docker run -d -p 8080:8080 --name java-jasper <your-registry>/java-jasper:non-prod

✅ Production Notes

Ensure the following for production readiness:

Monitoring (e.g., Prometheus, Grafana)

Centralized logging (e.g., ELK Stack)

Production-grade database (e.g., PostgreSQL, MySQL)

HTTPS via reverse proxy (e.g., Nginx, Traefik)

Secure handling of secrets and environment variables

⚠️ These enhancements are not yet implemented in this prototype.

🧪 4. Run Tests

Run All Tests

mvn clean test

clean – Removes the /target directorytest – Compiles and runs all tests in src/test/java

Skip Tests During Build

mvn clean package -DskipTests

Run a Specific Test Class

mvn -Dtest=ClassNameTest test

Replace ClassNameTest with one of the following:

Test Class

Description

JavaJasperApplicationTests

Validates user input and basic functionality

UserReportTest

Verifies Jasper report generation

PaginatedUserAndCountTest

Tests user inquiry and filter pagination

📁 Local Configuration Example

Modify src/main/resources/application.properties to adjust local settings:

server.port=8080
spring.datasource.url=jdbc:h2:mem:manulifedb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

🤝 Contribution

Feel free to fork or clone this repo, open issues, and submit PRs!