package com.takeaway.gameof3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Created by Shwetha on 09-12-2018.
 */
@Service
public class NumberSendingService {
    private static final Logger log = LoggerFactory.getLogger(NumberSendingService.class);

    private  String player2Url;
    private  String player2Port;
    private  RestTemplate restTemplate;

    @Autowired
    public NumberSendingService(String player2Url, String player2Port, RestTemplate restTemplate) {
        this.player2Url = player2Url;
        this.player2Port = player2Port;
        this.restTemplate = restTemplate;
    }

    public Optional<ResponseEntity<Void>> send(int number) {
        try {
            String player2UrlForSendingNumber = getPlayer2UrlForSendingNumber(number);
            ResponseEntity<Void> voidResponseEntity = restTemplate.postForEntity(player2UrlForSendingNumber, Void.class, Void.class);

            if (voidResponseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
                log.info("Send the random number generated to Player 2");
                return Optional.of(voidResponseEntity);
            }
        }
        catch(HttpClientErrorException exception){
            log.warn("Player2 is unreachable due to exception : {}", exception.getMessage());
        }
        return Optional.empty();
    }

    public String getPlayer2UrlForSendingNumber(int number) {
        return player2Url + ":" + player2Port + "/gameof3/" + number;
    }
}
