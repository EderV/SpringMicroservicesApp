package com.evm.ms.notifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.evm.ms.notifier.application.notifier.AndroidServiceImpl;
import com.evm.ms.notifier.domain.AndroidData;
import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.config.AndroidConfig;
import com.evm.ms.notifier.domain.ports.out.NotifierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AndroidServiceImplTest {

    @Mock
    private NotifierPort<AndroidData> notifierPort;

    @InjectMocks
    private AndroidServiceImpl androidService;

    private List<AndroidConfig> androidConfigs;
    private Event event;

    @BeforeEach
    public void setUp() {
        AndroidConfig androidConfig = new AndroidConfig();
        androidConfig.setToken("testToken");
        androidConfig.setIdentifier("testTopic");

        androidConfigs = List.of(androidConfig);

        event = new Event();
        event.setTitle("Test Title");
        event.setDescription("Test Description");
    }

    @Test
    public void testSendNotification_Success() throws ExecutionException, InterruptedException {
        when(notifierPort.sendNotification(any(AndroidData.class))).thenReturn(true);

        CompletableFuture<Void> future = androidService.sendNotification(androidConfigs, event);
        future.get();

        verify(notifierPort, times(1)).sendNotification(any(AndroidData.class));
    }

    @Test
    public void testSendNotification_Failure() throws ExecutionException, InterruptedException {
        when(notifierPort.sendNotification(any(AndroidData.class))).thenReturn(false);

        CompletableFuture<Void> future = androidService.sendNotification(androidConfigs, event);
        future.get();

        verify(notifierPort, times(1)).sendNotification(any(AndroidData.class));
    }

    @Test
    public void testSendNotification_MultipleConfigs() throws ExecutionException, InterruptedException {
        AndroidConfig anotherConfig = new AndroidConfig();
        anotherConfig.setToken("anotherToken");
        anotherConfig.setIdentifier("anotherTopic");

        List<AndroidConfig> multipleConfigs = List.of(androidConfigs.get(0), anotherConfig);

        when(notifierPort.sendNotification(any(AndroidData.class))).thenReturn(true);

        CompletableFuture<Void> future = androidService.sendNotification(multipleConfigs, event);
        future.get();

        verify(notifierPort, times(2)).sendNotification(any(AndroidData.class));
    }
}
