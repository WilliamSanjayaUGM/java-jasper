Deployment & Setup Instruction Guide:

1. Running Locally
Requirements:
- Java 21
- Maven 3.9+
Steps:
# Clone the project
$ git clone git@github.com:WilliamSanjayaUGM/java-jasper.git
$ cd java-jasper
# Run the application:
$ mvn clean spring-boot:run

Open in browser:
UI: http://localhost:8080
H2 Console: http://localhost:8080/h2-console

# If you want to config locally, just config the application.properties inside: src/main/resources/application.properties
# ex: if you want to change the localhost:8080 become localhost:8081 -> server.port=8081

2. Docker Deployment (Non-Prod & Prod)
2.1 The DockerFile is already created
2.2 docker-compose.yml is also already created.
    - To run locally, use docker-compose.override.yml. Here is the example of docker-compose for local:

############ Start #################
docker-compose.override.yml:
# For local development
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
############ End #################

    - To run on prod, use docker-compose.prod.yml. And adjust this part:
        - image: your-registry/java-jasper:prod -> to use your image
        - And also the SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD -> by using whatever database you're using
2.3 Docker commands:
# Local Dev:
docker-compose up --build

# Production:
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

3. Deploy to Non-Prod & Prod
General Instructions:

Build image and push to registry (DockerHub, GitHub Container Registry, or private)
$ docker tag java-jasper:latest <your-registry>/java-jasper:non-prod
$ docker push <your-registry>/java-jasper:non-prod

On your server or cloud:

$ docker pull <your-registry>/java-jasper:non-prod
$ docker run -d -p 8080:8080 --name java-jasper <your-registry>/java-jasper:non-prod

For Production:
- Set up monitoring (Prometheus, Grafana, etc.)
- Set up logging (ELK stack, etc.)
- Use a production-ready database
- Use HTTPS with reverse proxy (e.g., Nginx, Traefik)
- Configure environment variables securely
Which all of the above hasn't been added yet in this prototype & testing project