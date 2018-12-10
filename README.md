### Build Status

[![Build Status](https://travis-ci.org/shwethadavenapalli/GameOf3.svg?branch=master)](https://travis-ci.org/shwethadavenapalli/GameOf3)


### About the Game

The goal is to implement a game with two independent agents – the “players” –
communicating with each other using an interface.
When a player starts, a random whole number is generated and sent to the other player, which indicates the start of the game. The receiving player must now add one of { -1, 0, 1 } to get a number that is divisible by 3 and then divide it. The resulting number is then sent back to the original sender.
The same rules apply until one of the players reaches the number 1 after division, which ends the game.

### Game implementation 

Created a Spring boot application which can run for in 2 modes for each player.

- Player 1 is the game initiator and runs on port `8080` and sends the first number to player
- Player 2 waits for the input number and when received, divides the number by 3 and sends it to player 1
- If either of the player receives 3, then the GAME is WON by that player.
- The player who won will send the WON status to the oponent player.

This application exposes endpoints for message passing between players
 ```
POST -  /gameof3/{inputNumber}
POST - /status/{playerName}/WON
 ```

### How to build 
 - Run the `sh build.sh` which runs `mvn clean install`

### How to start the game 
- Run `sh startPaly.sh`
- The logs for each each player will be generated in `./player1` and `./player2`

### How to stop the game
- Run `sh killAllPlayers.sh`

### Improvements to be worked upon. 
- The application does not gets ended after the game is won.
