package com.takeaway.gameof3.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Created by Shwetha on 10-12-2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class NumberSendingExecutorServiceTest {

    @Mock
    private MessagingService service;

    private NumberSendingExecutorService executorService;

    @Before
    public void setup() {
        executorService = new NumberSendingExecutorService(service);
    }

    @Test
    public void shouldDelegateTheJobOfSendingtoNumberSendingService() throws Exception {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);
        given(service.send(10)).willReturn(Optional.of(responseEntity));

        executorService.send(10);

        Thread.sleep(2000);
        verify(service, times(1)).send(10);
        verifyNoMoreInteractions(service);
    }
}