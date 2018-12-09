package com.takeaway.gameof3.controller;

import com.takeaway.gameof3.service.NumberSendingService;
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
    NumberSendingService numberSendingService;

    private static final Logger log = LoggerFactory.getLogger(GameOf3Controller.class);

    @RequestMapping(value = "/{inputNumber}", method = RequestMethod.POST)
    public ResponseEntity<Void> processRequest(@PathVariable Integer inputNumber) {
        log.info("GameOf3Controller.processRequest");
        log.info("inputNumber = [" + inputNumber + "]");
        Optional<ResponseEntity<Void>> isSentToNextPlayer = numberSendingService.send(inputNumber);
        if (isSentToNextPlayer.isPresent() && isSentToNextPlayer.get().getStatusCode() == HttpStatus.ACCEPTED)
            return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
    }

}