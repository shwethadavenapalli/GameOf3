package com.takeaway.gameof3.controller;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Shwetha on 09-12-2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "game.initiator=true",
        "player1.url=http://localhost",
        "player1.port=8080",
        "player2.url=http://localhost",
        "player2.port=8081",
})
public class GameOf3ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(8081);


    @Test
    public void givenNumberShouldBeSentToOpponentPlayerSuccessfully() throws Exception {
        setupStubForPlayer2ForAcceptingANumberAndReturn202(3);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/gameof3/10");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());

        Thread.sleep(2000);
        verify(postRequestedFor(urlMatching("/gameof3/" + 3)));
    }

    @Test
    public void shouldSendGameStatusAsWonToOpponent() throws Exception {
        String playerName = "Player1";
        String gameStatus = "WON";

        setupStubForAcceptingGameAsWonToPlayer2(playerName, gameStatus);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/gameof3/3");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(postRequestedFor(urlMatching("/gameof3/status/" + playerName + "/" + gameStatus)));
    }

    public void setupStubForPlayer2ForAcceptingANumberAndReturn202(Integer number) {
        stubFor(post(urlMatching("/gameof3/" + number))
                .willReturn(aResponse()
                        .withStatus(202)));
    }

    public void setupStubForAcceptingGameAsWonToPlayer2(String playerName, String gameStatus) {
        stubFor(post(urlMatching("/gameof3/status/" + playerName + "/" + gameStatus))
                .willReturn(aResponse()
                        .withStatus(200)));
    }

}