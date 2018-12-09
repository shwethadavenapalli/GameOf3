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
    public boolean isGameInitator() {
        return gameInitator;
    }

    public String getPlayer1Url() {
        return player1Url;
    }

    public void setPlayer1Url(String player1Url) {
        this.player1Url = player1Url;
    }

    public String getPlayer1Port() {
        return player1Port;
    }

    public void setPlayer1Port(String player1Port) {
        this.player1Port = player1Port;
    }

    public String getPlayer2Url() {
        return player2Url;
    }

    public void setPlayer2Url(String player2Url) {
        this.player2Url = player2Url;
    }

    public String getPlayer2Port() {
        return player2Port;
    }

    public void setPlayer2Port(String player2Port) {
        this.player2Port = player2Port;
    }
}
