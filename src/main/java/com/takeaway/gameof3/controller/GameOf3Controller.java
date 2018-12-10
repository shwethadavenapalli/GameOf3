package com.takeaway.gameof3.controller;

import com.takeaway.gameof3.service.NumberSendingService;
import com.takeaway.gameof3.util.RoundToNearestFactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by Shwetha on 09-12-2018.
 */
@RestController
@RequestMapping(value = "/gameof3")
public class GameOf3Controller {

    @Autowired
    private NumberSendingService numberSendingService;

    private static final Logger log = LoggerFactory.getLogger(GameOf3Controller.class);

    @RequestMapping(value = "/{inputNumber}", method = RequestMethod.POST)
    public ResponseEntity<Void> processRequest(@PathVariable Integer inputNumber) {
        log.info("Received number : {} by : {}", inputNumber, numberSendingService.getPlayerName());

        Integer roundedInputNumber = RoundToNearestFactor.roundToNearestFactorOf3(inputNumber);

        if (!IsGameWonByCurrentPlayer(roundedInputNumber)) {

            int reducedNumber = roundedInputNumber / 3;

            Optional<ResponseEntity<Void>> isSentToNextPlayer = numberSendingService.send(reducedNumber);

            if (isSentToNextPlayer.isPresent() && isSentToNextPlayer.get().getStatusCode() == HttpStatus.ACCEPTED)
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            else
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        log.info("{} has WON !!! :)", numberSendingService.getPlayerName());

        //convey to game status as WON to oponent player
        numberSendingService.sendGameStatusAsWON();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/status/{playerName}/WON", method = RequestMethod.POST)
    public ResponseEntity<Void> processRequest(@PathVariable String playerName) {

        log.info("Received that {} has won.", playerName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean IsGameWonByCurrentPlayer(@PathVariable Integer inputNumber) {
        return inputNumber / 3 == 1;
    }

}
