package com.takeaway.gameof3.domain;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.takeaway.gameof3.config.GameConfig;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Shwetha on 08-12-2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GameConfig.class})
@TestPropertySource(properties = {
        "player2.url=http://localhost",
        "player2.port=8081",
})
public class GameInitiatorTest {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(8081);

    @Autowired
    private GameInitiator gameInitiator;

    @Test
    public void shouldGenerateRandomNumberAnd_MakeAtMost_3AttemptsToSendNumber_WhenPlayer2IsOffline() throws InterruptedException {
        gameInitiator.run();
        assertThat(gameInitiator.getRetryAttemptPerformed()).isEqualTo(3);
    }

    @Test
    public void shouldSendRandomNumberToPlayer2_WhenPlayer2IsOnline() throws Exception {
        setupStubForPlayer2ForAcceptingANumberAndReturn200(10);
        gameInitiator.send(10);
        assertThat(gameInitiator.getRetryAttemptPerformed()).isEqualTo(0);
    }

    public void setupStubForPlayer2ForAcceptingANumberAndReturn200(Integer number){
        stubFor(post(urlMatching("/gameof3/"+number))
                .willReturn(aResponse()
                        .withStatus(202)));
    }
}