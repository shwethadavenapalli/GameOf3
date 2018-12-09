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

    private String playerUrl;
    private String playerPort;
    private RestTemplate restTemplate;


    @Autowired
    public NumberSendingService(@Value("${player2.url}") String playerUrl,
                                @Value("${player2.port}") String playerPort,
                                RestTemplate restTemplate) {
        this.playerUrl = playerUrl;
        this.playerPort = playerPort;
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
        } catch (HttpClientErrorException exception) {
            log.warn("Player2 is unreachable due to exception : {}", exception.getMessage());
        }
        return Optional.empty();
    }

    public String getPlayer2UrlForSendingNumber(int number) {
        return playerUrl + ":" + playerPort + "/gameof3/" + number;
    }
}
