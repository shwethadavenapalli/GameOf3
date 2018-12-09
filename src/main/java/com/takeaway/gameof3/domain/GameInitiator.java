package com.takeaway.gameof3.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * Created by Shwetha on 08-12-2018.
 */
@Component
public class GameInitiator implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GameInitiator.class);

    private String player2Url;

    private final String player2Port;
    private final RestTemplate restTemplate;
    private static final int MAX_RETRY_COUNT = 3;
    private int retryAttemptsPerformed;

    @Autowired
    public GameInitiator(String player2Url, String player2Port, RestTemplate restTemplate) {
        this.player2Url = player2Url;
        this.player2Port = player2Port;
        this.restTemplate = restTemplate;
    }

    @Override
    public void run() {
        log.info("Sending a random number to player2");
        int number = getRandomNumber();
        send(number);
    }

    private int getRandomNumber() {
        Random random = new Random();
        int min = 3;
        int max = Integer.MAX_VALUE;
        return random.nextInt((max - min) + 1) + min;
    }

    public void send(int number) {
        ResponseEntity<Void> voidResponseEntity;
        do {
            try {
                voidResponseEntity = restTemplate.postForEntity(getPlayer2UrlForSendingNumber(number), Void.class, Void.class);

                if (voidResponseEntity.getStatusCode() == HttpStatus.OK) {
                    log.info("Send the random number generated to Player 2");
                    return;
                }

            } catch (ResourceAccessException resourceAccessException) {
                log.warn("Could not reach player 2 endpoint :{}", player2Url);
                retryAttemptsPerformed++;
            }

        } while (retryAttemptsPerformed < MAX_RETRY_COUNT);
    }

    public int getRetryAttemptPerformed() {
        return retryAttemptsPerformed;
    }

    private String getPlayer2UrlForSendingNumber(int number) {
        return player2Url + ":" + player2Port + "/gameof3/" + number;
    }
}
