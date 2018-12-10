package com.takeaway.gameof3.controller;

import com.takeaway.gameof3.service.NumberSendingExecutorService;
import com.takeaway.gameof3.service.MessagingService;
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

/**
 * Created by Shwetha on 09-12-2018.
 */
@RestController
@RequestMapping(value = "/gameof3")
public class GameOf3Controller {

    private MessagingService messagingService;
    private NumberSendingExecutorService executorService;

    @Autowired
    public GameOf3Controller(MessagingService messagingService, NumberSendingExecutorService executorService) {
        this.messagingService = messagingService;
        this.executorService = executorService;
    }

    private static final Logger log = LoggerFactory.getLogger(GameOf3Controller.class);

    @RequestMapping(value = "/{inputNumber}", method = RequestMethod.POST)
    public ResponseEntity<Void> acceptInputNumberFromOponent(@PathVariable Integer inputNumber) {
        log.info("Received number : {} by : {}", inputNumber, messagingService.getPlayerName());

        Integer roundedInputNumber = RoundToNearestFactor.roundToNearestFactorOf3(inputNumber);

        if (!IsGameWonByCurrentPlayer(roundedInputNumber)) {

            int reducedNumber = roundedInputNumber / 3;
            executorService.send(reducedNumber);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        log.info("{} has WON !!! :)", messagingService.getPlayerName());

        //convey game status as WON to oponent player
        messagingService.sendGameStatusAsWON();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/status/{playerName}/WON", method = RequestMethod.POST)
    public ResponseEntity<Void> acceptGameStatus(@PathVariable String playerName) {

        log.info("Received that {} has won.", playerName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean IsGameWonByCurrentPlayer(@PathVariable Integer inputNumber) {
        return inputNumber / 3 == 1;
    }

}
