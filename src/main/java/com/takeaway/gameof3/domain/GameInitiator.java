package com.takeaway.gameof3.domain;

import com.takeaway.gameof3.service.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final MessagingService service;
    private static final int MAX_RETRY_COUNT = 3;
    private int retryAttemptsPerformed;

    @Autowired
    public GameInitiator(MessagingService service) {
        this.service = service;
    }

    @Override
    public void run() {
        log.info("Game Initiator has started and trying to send a random number");
        send(getRandomNumber());
    }


    public void send(int number) {
        //reset the attempt counter
        resetAttemptCoutner();

        //perform an attempt to send a number to player 2,
        //Max attempts is 3 times. After that player 1 refuses to play :(
        do {
            log.info("{} : Sending a random number : {} to oponent : {} ",
                    service.getPlayerName(), number, service.getOponentPlayerName());

            Optional<ResponseEntity<Void>> response = service.send(number);
            if (!response.isPresent()) {
                retryAttemptsPerformed++;
                sleepExponentially();
            } else {
                break;
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
