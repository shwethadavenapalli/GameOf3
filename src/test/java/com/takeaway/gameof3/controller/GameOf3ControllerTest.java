package com.takeaway.gameof3.controller;

import com.takeaway.gameof3.service.NumberSendingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by Shwetha on 09-12-2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = GameOf3Controller.class, secure = false)
public class GameOf3ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    NumberSendingService numberSendingServiceMock;

    @Test
    public void givenNumberShouldBeProcessed_AndSentToNextPlayer_Successfully() throws Exception {

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);

        Mockito.when(numberSendingServiceMock.send(50)).thenReturn(Optional.of(responseEntity));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/gameof3/50");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
    }

    @Test
    public void givenNumberShouldBeProcessedAnd_ReturnEmptyStatus_WhenNextPlayerIsOffline() throws Exception {

        Mockito.when(numberSendingServiceMock.send(50)).thenReturn(Optional.empty());
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/gameof3/50");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());
    }
}
