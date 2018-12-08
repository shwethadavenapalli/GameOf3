package com.takeaway.gameof3.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Shwetha on 08-12-2018.
 */
@Component
public class GameInitiator implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GameInitiator.class);

    private final RestTemplate restTemplate;
    private static final int MAX_RETRY_COUNT = 3;
    private int retryAttempts;

    private String player2Url = "http://localhost:8081";

    @Autowired
    public GameInitiator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run() {
        log.info("Sending a number to player2");
        send();
    }

    public void send() {
        ResponseEntity<Void> voidResponseEntity = null;
        do {
            try {
                voidResponseEntity = restTemplate.postForEntity(player2Url, 10d, Void.class);
            }
            catch( ResourceAccessException resourceAccessException){
                log.warn("Could not reach player 2 endpoint :{}", player2Url);
                retryAttempts++;
            }

        } while (retryAttempts < MAX_RETRY_COUNT);
    }

    public int getMaxRetriedCount() {
        return retryAttempts;
    }
}
