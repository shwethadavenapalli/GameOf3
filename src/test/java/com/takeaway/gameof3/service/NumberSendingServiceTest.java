package com.takeaway.gameof3.service;

import com.takeaway.gameof3.config.OponentEndpointSelector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Shwetha on 09-12-2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class NumberSendingServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private NumberSendingService numberSendingService;

    @Before
    public void setUp() throws Exception {
        OponentEndpointSelector oponentEndpointSelector = getOponentEndpointSelector();
        numberSendingService = new NumberSendingService(oponentEndpointSelector, restTemplate);
    }

    @Test
    public void shouldSendANumberToPlayer2Appplication() throws Exception {

        //Arrange
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);
        int numberToSend = 20;

        String player2Url = numberSendingService.getPlayer2UrlForSendingNumber(numberToSend);

        when(restTemplate.postForEntity(player2Url, Void.class, Void.class))
                .thenReturn(responseEntity);

        //Act
        Optional<ResponseEntity<Void>> send = numberSendingService.send(numberToSend);

        //Assert
        assertThat(send).isPresent();
        assertThat(send.get().getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        verify(restTemplate, times(1)).postForEntity(player2Url, Void.class, Void.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void shouldThrowPlaye2NotAvailableExceptionIfPlayer2IsUnreachable() throws Exception {

        //Arrange
        int numberToSend = 20;
        String player2Url = numberSendingService.getPlayer2UrlForSendingNumber(numberToSend);

        when(restTemplate.postForEntity(player2Url, Void.class, Void.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        //Act
        Optional<ResponseEntity<Void>> send = numberSendingService.send(numberToSend);
        assertThat(send).isEmpty();

        //Assert
        verify(restTemplate, times(1)).postForEntity(player2Url, Void.class, Void.class);
        verifyNoMoreInteractions(restTemplate);
    }

    private OponentEndpointSelector getOponentEndpointSelector() {
        OponentEndpointSelector oponentEndpointSelector = new OponentEndpointSelector();
        oponentEndpointSelector.setGameInitator(true);
        oponentEndpointSelector.setPlayer1Url("http://localhost");
        oponentEndpointSelector.setPlayer2Url("http://localhost");
        oponentEndpointSelector.setPlayer1Port("8080");
        oponentEndpointSelector.setPlayer2Port("8081");
        return oponentEndpointSelector;
    }
}