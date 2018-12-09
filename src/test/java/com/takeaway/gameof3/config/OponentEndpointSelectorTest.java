package com.takeaway.gameof3.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Shwetha on 09-12-2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GameConfig.class})
@TestPropertySource(properties = {
        "game.initiator=true",
        "player1.url=http://localhost",
        "player1.port=8080",
        "player2.url=http://localhost",
        "player2.port=8081",
})
public class OponentEndpointSelectorTest {

    @Autowired
    private OponentEndpointSelector selector;

    @Test
    public void shouldSelectRightOpponentURLFromApplicationProperties() throws Exception {
        String openentUrl = selector.getOpenentUrl();
        assertThat(openentUrl).isEqualTo("http://localhost:8081");

        selector.setGameInitator(false);
        String newOponentUrl = selector.getOpenentUrl();
        assertThat(newOponentUrl).isEqualTo("http://localhost:8080");
    }
}