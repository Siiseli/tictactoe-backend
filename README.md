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
    - ```docker-compose build tictactoe```
    - ```docker-compose up tictactoe```
- Server is now available at http://localhost:8080

If you just want to build the container for later use: 

- ```docker-compose build tictactoe```

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


## Usage

### Start game

- To start a new game, make a POST request to http://localhost:8080/game with request body as JSON containing
    - player name as "name"
    - selected character as "character"
        - Valid characters are "x" or "o"
- Example: 
```
{
    "name": "player name",
    "character": "x"
}
```
- The returning message will include the game ID
- Example: 
```
{
    "id": "fd280f19-7fd4-4760-b6ec-91d4eca2d7be"
}
```

### View all games

- To view all games, make a GET request http://localhost:8080/game
- The server will return all games in JSON

### View game state

- To view the state of any running game, make a GET request to http://localhost:8080/game/{id}
- For example ```http://localhost:8080/game/fd280f19-7fd4-4760-b6ec-91d4eca2d7be```
    - You can get the game state in ASCII representation by specifying "accept" http header as type "text/plain"
    - You can get the game state in JSON by specifying "accept" http header as type "application/json"

### Make move
- To make a move to any running game, make a POST request to http://localhost:8080/game/{id}/move with 
request body as JSON containing 
    - Row as "row"
    - Column as "col"
    - Valid values are "A", "B", "C"
    - Can not make move to already occupied position
    - Can not make moves when game has ended
- Example:
```
{
    "row": "A"
    "col": "A"
}
```
- The server will return the updated game state for convenience after computer has made its move
    - You can get the game state in ASCII representation by specifying "accept" http header as type "text/plain"
    - You can get the game state in JSON by specifying "accept" http header as type "application/json"
