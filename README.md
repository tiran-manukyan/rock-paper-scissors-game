# Rock Paper Scissors Game

Rock Paper Scissors Socket Game is a classic multiplayer game implemented using Java sockets.
Players can connect to a server and play Rock Paper Scissors against each other in real-time.
With its simple yet addictive gameplay, it offers a fun experience for players of all ages.

### Key Features

- **Real-Time Multiplayer:** Play against friends or random opponents in real-time.
- **Simple Interface:** Easy-to-use interface for seamless gameplay.

### Gameplay
Rock Paper Scissors Socket Game follows the traditional rules of Rock Paper Scissors.
Players choose one of three options (Rock, Paper, or Scissors), and the winner is determined based on the choices made.
The game supports multiplayer functionality, allowing players to challenge each other and compete for victory.


## Local Run
### Requirements
- Java 17

### Build
```shell
./gradlew clean build
```

### Run
```shell
java -jar build/libs/rock.jar
```

## Docker Run

### Build image
```shell
docker build -t rock .
```

### Run container
```shell
docker run -p 7815:7815 -d rock
```

### Connect to the game
```shell
telnet localhost 7815 
```
