package com.takeaway.gameof3.controller;

import com.takeaway.gameof3.service.NumberSendingExecutorService;
import com.takeaway.gameof3.service.MessagingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by Shwetha on 09-12-2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = GameOf3Controller.class, secure = false)
public class GameOf3ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessagingService serviceMock;

    @MockBean
    private NumberSendingExecutorService executorService;

    @Test
    public void givenNumberNotDivisibleBy3_ShouldRoundToNearestFactorOf3_AndSendComputedNumberToOpponentPlayer_Successfully() throws Exception {

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);

        MvcResult result = mockMvc.perform(post("/gameof3/50"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());

        verify(executorService, times(1)).send(17);
    }

    @Test
    public void givenNumberDivisibleBy3_ShouldRoundToNearestFactorOf3_AndSendComputedNumberToOpponentPlayer_Successfully() throws Exception {

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);

        MvcResult result = mockMvc.perform(post("/gameof3/51"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());

        Thread.sleep(2000);
        verify(executorService, times(1)).send(17);
    }

    @Test
    public void givenNumberDivisibleBy3_ShouldRoundToNearestFactorOf3_AndDeclareGameIsWon_WhenComputedNumberIs1() throws Exception {

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        when(serviceMock.sendGameStatusAsWON()).thenReturn(Optional.of(responseEntity));

        MvcResult result = mockMvc.perform(post("/gameof3/3")).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(serviceMock, times(1)).sendGameStatusAsWON();
    }

    @Test
    public void givenNumberShouldBeProcessedAnd_ReturnEmptyStatus_WhenNextPlayerIsOffline() throws Exception {

        RequestBuilder requestBuilder =
                post("/gameof3/50");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
        verify(executorService, times(1)).send(17);
    }

    @Test
    public void shouldAcceptGameStatusAsWONFromOponentPlayer() throws Exception {

        MvcResult result = mockMvc.perform(post("/gameof3/status/Player1/WON")).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }
}
