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
public class MessagingServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private MessagingService messagingService;

    @Before
    public void setUp() throws Exception {
        OponentEndpointSelector oponentEndpointSelector = getOponentEndpointSelector();
        messagingService = new MessagingService(oponentEndpointSelector, restTemplate);
    }

    @Test
    public void shouldSendANumberToPlayer2Appplication() throws Exception {

        //Arrange
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);
        int numberToSend = 20;

        String player2UrlForSendingNumber = messagingService.getPlayer2UrlForSendingNumber(numberToSend);

        when(restTemplate.postForEntity(player2UrlForSendingNumber, Void.class, Void.class))
                .thenReturn(responseEntity);

        //Act
        Optional<ResponseEntity<Void>> response = messagingService.send(numberToSend);

        //Assert
        assertThat(response).isPresent();
        assertThat(response.get().getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        verify(restTemplate, times(1)).postForEntity(player2UrlForSendingNumber, Void.class, Void.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void shouldReturnEmptyOptionalIfPlayer2IsUnreachableForSengindANumber() throws Exception {

        //Arrange
        int numberToSend = 20;
        String player2UrlForSendingNumber = messagingService.getPlayer2UrlForSendingNumber(numberToSend);

        when(restTemplate.postForEntity(player2UrlForSendingNumber, Void.class, Void.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        //Act
        Optional<ResponseEntity<Void>> response = messagingService.send(numberToSend);
        assertThat(response).isEmpty();

        //Assert
        verify(restTemplate, times(1)).postForEntity(player2UrlForSendingNumber, Void.class, Void.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void shouldSendGameWonStatusToPlayer2Appplication() throws Exception {

        //Arrange
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        String enpointForSendingGameStatus = messagingService.getEnpointForSendingGameStatus();

        when(restTemplate.postForEntity(enpointForSendingGameStatus, Void.class, Void.class))
                .thenReturn(responseEntity);

        //Act
        Optional<ResponseEntity<Void>> response = messagingService.sendGameStatusAsWON();

        //Assert
        assertThat(response).isPresent();
        assertThat(response.get().getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(restTemplate, times(1)).postForEntity(enpointForSendingGameStatus, Void.class, Void.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void shouldReturnEmptyOptionalIfPlayer2IsUnreachableForSendingGameStatus() throws Exception {

        String enpointForSendingGameStatus = messagingService.getEnpointForSendingGameStatus();

        when(restTemplate.postForEntity(enpointForSendingGameStatus, Void.class, Void.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        //Act
        Optional<ResponseEntity<Void>> response = messagingService.sendGameStatusAsWON();

        //Assert
        assertThat(response).isEmpty();
        verify(restTemplate, times(1)).postForEntity(enpointForSendingGameStatus, Void.class, Void.class);
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