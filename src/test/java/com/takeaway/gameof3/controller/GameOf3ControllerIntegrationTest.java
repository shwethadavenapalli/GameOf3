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
        setupStubForPlayer2ForAcceptingANumberAndReturn202(9);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/gameof3/10");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
    }

    @Test
    public void shouldReturnEmptyValueWhenOpponentPlayerIsOffline() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/gameof3/50");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());
    }

    public void setupStubForPlayer2ForAcceptingANumberAndReturn202(Integer number) {
        stubFor(post(urlMatching("/gameof3/" + number.intValue()))
                .willReturn(aResponse()
                        .withStatus(202)));
    }


}