# Tic tac toe backend

This is a tic tac toe REST service written in Kotlin, using Spring Boot and Jersey.

## Starting up

### With Docker

#### Prerequisites
- Install Docker

#### Building jar

- To build the jar, run
    - ```docker-compose build builder```
    - ```docker-compose run builder```
- Build results can now be found in /target

#### Running

- After building the jar, docker container can be started with
    - ```docker-compose build server```
    - ```docker-compose up server```
- Server is now available at http://localhost:8080

If you just want to build the container for later use: 

- ```docker-compose build server```

### Without Docker

#### Prerequisites
- Install Maven
- Install JDK 8+

#### Building
- Run ```./mvnw install```
    
#### Running
- Run ```java -jar target/tictactoe-backend-0.0.1-SNAPSHOT.jar```

#### Running without building jar (Useful for development)
- Run ```mvn spring-boot:run```
- Server is now available at localhost:8080
