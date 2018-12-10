package com.takeaway.gameof3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Shwetha on 10-12-2018.
 */
@Service
public class NumberSendingExecutorService {

    private static final Logger log = LoggerFactory.getLogger(NumberSendingExecutorService.class);

    private NumberSendingService numberSendingService;

    @Autowired
    public NumberSendingExecutorService(NumberSendingService numberSendingService) {
        this.numberSendingService = numberSendingService;
    }

    public void send(int number){
        Runnable sendNumberToOponentTask = () -> {
            Optional<ResponseEntity<Void>> isSentToNextPlayer = numberSendingService.send(number);
            if (isSentToNextPlayer.isPresent() && isSentToNextPlayer.get().getStatusCode() == HttpStatus.ACCEPTED){
                log.debug("Opponent accepted the number");
            } else {
                log.debug("Opponent did not accepted the number");
            }
        };
        Thread thread = new Thread(sendNumberToOponentTask);
        thread.start();
    }
}
