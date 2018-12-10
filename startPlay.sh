#!/usr/bin/env bash

echo "Starting the game of three"

cd player2

java -jar ../target/game_of_3-0.0.1-SNAPSHOT.jar  --spring.config.location=application.properties > player2.log &

sleep 10

cd ../player1/

java -jar ../target/game_of_3-0.0.1-SNAPSHOT.jar  --spring.config.location=application.properties > player1.log &

cd ..
