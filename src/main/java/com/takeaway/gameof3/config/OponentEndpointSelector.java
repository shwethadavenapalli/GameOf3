package com.takeaway.gameof3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Shwetha on 09-12-2018.
 */
@Component
public class OponentEndpointSelector {

    @Value("${game.initiator}")
    private boolean gameInitator;

    @Value("${player1.url:}")
    String player1Url;
    @Value("${player1.port:}")
    String player1Port;

    @Value("${player2.url:}")
    String player2Url;
    @Value("${player2.port:}")
    String player2Port;

    public String getOpenentUrl() {
        if (gameInitator) {
            return player2Url + ":" + player2Port;
        }
        return player1Url + ":" + player1Port;
    }

    public void setGameInitator(boolean gameInitator) {
        this.gameInitator = gameInitator;
    }
}
