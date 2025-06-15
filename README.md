# Java Jasper Deployment & Setup Guide

This guide explains how to run, build, and deploy the `java-jasper` Spring Boot + Vaadin + JasperReports application.

---

## 🚀 1. Running Locally

### Requirements
- Java 21
- Maven 3.9+

### Steps
```bash / cmd
# Clone the repository
git clone git@github.com:WilliamSanjayaUGM/java-jasper.git
cd java-jasper

# Run the application
mvn clean spring-boot:run
````

### Access

* UI: [http://localhost:8080](http://localhost:8080)
* H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

> 💡 To configure the app locally, edit:
> `src/main/resources/application.properties`
> Example: Change the port by setting `server.port=8081`

---

## 🐳 2. Docker Deployment (Non-Prod & Prod)

### 2.1 Files You Need

* `Dockerfile` – Already created
* `docker-compose.yml` – Base setup
* `docker-compose.override.yml` – For local development
* `docker-compose.prod.yml` – For production config

---

### 2.2 Running with Docker Compose

#### ✅ Local Development

Example of `docker-compose.override.yml`:

```
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
```

Run locally:

```bash
docker-compose up --build
```

#### 🚀 Production Environment

Adjust in `docker-compose.prod.yml`:

* Replace `image: your-registry/java-jasper:prod` with your real image
* Update the database credentials in `SPRING_DATASOURCE_URL`, `USERNAME`, and `PASSWORD`

Run in prod:

```bash
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

---

## ☁️ 3. Deploying to Non-Prod & Prod

### Build & Push Docker Image

```bash
docker build -t java-jasper:latest .
docker tag java-jasper:latest <your-registry>/java-jasper:non-prod
docker push <your-registry>/java-jasper:non-prod
```

### Run on Server

```bash
docker pull <your-registry>/java-jasper:non-prod
docker run -d -p 8080:8080 --name java-jasper <your-registry>/java-jasper:non-prod
```

---

### ✅ Production Notes

For a real deployment:

* ✅ Set up monitoring (e.g., Prometheus, Grafana)
* ✅ Centralize logging (e.g., ELK Stack)
* ✅ Use a persistent, production-ready database
* ✅ Use HTTPS via Nginx or Traefik
* ✅ Secure environment configs

> ⚠️ All of these production aspects are **not yet implemented** in this prototype.

---

## 🧪 4. Run Tests

To run unit/integration tests:

```bash
mvn clean test
```

---

## 📁 Local Configuration Example

Adjust `src/main/resources/application.properties` as needed:

```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:manulifedb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

---

## 🤝 Contribution

Feel free to fork the repo or clone the repo!

---