package com.takeaway.gameof3.domain;

import com.takeaway.gameof3.service.NumberSendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.takeaway.gameof3.util.RandomNumberGenerator.getRandomNumber;

/**
 * Created by Shwetha on 08-12-2018.
 */
@Component
public class GameInitiator implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GameInitiator.class);

    private final NumberSendingService service;
    private static final int MAX_RETRY_COUNT = 3;
    private int retryAttemptsPerformed;

    @Autowired
    public GameInitiator(NumberSendingService service) {
        this.service = service;
    }

    @Override
    public void run() {
        log.info("Game Initiator");
        send(getRandomNumber());
    }


    public void send(int number) {
        //reset the attempt counter
        resetAttemptCoutner();

        //perform an attempt to send a number to player 2,
        //Max attempts is 3 times. After that player 1 refuses to play :(
        do {
            Optional<ResponseEntity<Void>> response = service.send(number);
            if (!response.isPresent()) {
                retryAttemptsPerformed++;
                sleepExponentially();
            } else {
                return;
            }
        } while (retryAttemptsPerformed < MAX_RETRY_COUNT);
    }



    private void resetAttemptCoutner() {
        retryAttemptsPerformed = 0;
    }

    private void sleepExponentially() {
        double sleepForMicroSeconds = Math.pow(Double.valueOf(retryAttemptsPerformed), 2d);
        try {
            Thread.sleep(Double.valueOf(sleepForMicroSeconds * 1000).longValue());
        } catch (InterruptedException e) {
            log.error("Thread was interrupted while it was waiting for player 2 to revive");
        }
    }

    public int getRetryAttemptPerformed() {
        return retryAttemptsPerformed;
    }


}
