package com.evm.ms.notifier;

import com.evm.ms.notifier.application.notifier.AndroidService;
import com.evm.ms.notifier.application.notifier.EmailService;
import com.evm.ms.notifier.application.notifier.NotifierService;
import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.config.AndroidConfig;
import com.evm.ms.notifier.domain.config.EmailConfig;
import com.evm.ms.notifier.domain.config.NotificationConfig;
import com.evm.ms.notifier.domain.ports.out.MessengerOutputPort;
import com.evm.ms.notifier.domain.ports.out.NotificationConfigRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotifierServiceTest {

    @Mock
    private NotificationConfigRepositoryPort notificationConfigRepositoryPortMock;

    @Mock
    private AndroidService androidServiceMock;

    @Mock
    private EmailService emailServiceMock;

    @Mock
    private MessengerOutputPort messengerOutputPortMock;

    @InjectMocks
    private NotifierService notifierService;

    private Event event;
    private UUID userUuid;
    private NotificationConfig config;

    @BeforeEach
    public void setUp() {
        userUuid = UUID.randomUUID();
        event = new Event();
        event.setUserId(userUuid.toString());

        config = new NotificationConfig();
        config.setAndroids(List.of(new AndroidConfig()));
        config.setEmails(List.of(new EmailConfig()));
    }

    @Test
    public void notifyClient_shouldLogError_whenUserIdIsInvalid() {
        event.setUserId("invalid-uuid");

        notifierService.notifyClient(event);

        verify(notificationConfigRepositoryPortMock, never()).getNotificationConfigByUserUUID(any());
        verify(androidServiceMock, never()).sendNotification(any(), any());
        verify(emailServiceMock, never()).sendEmail(any(), any());
    }

    @Test
    public void notifyClient_shouldLogWarning_whenNoNotificationConfigExists() {
        when(notificationConfigRepositoryPortMock.getNotificationConfigByUserUUID(userUuid)).thenReturn(Optional.empty());

        notifierService.notifyClient(event);

        verify(androidServiceMock, never()).sendNotification(any(), any());
        verify(emailServiceMock, never()).sendEmail(any(), any());
    }

    @Test
    public void notifyClient_shouldSendNotification_whenAndroidConfigsExist() {
        config.setEmails(List.of());
        when(notificationConfigRepositoryPortMock.getNotificationConfigByUserUUID(userUuid)).thenReturn(Optional.of(config));

        notifierService.notifyClient(event);

        verify(androidServiceMock, times(1)).sendNotification(config.getAndroids(), event);
        verify(emailServiceMock, never()).sendEmail(any(), any());
    }

    @Test
    public void notifyClient_shouldSendEmail_whenEmailConfigsExist() {
        config.setAndroids(List.of());
        when(notificationConfigRepositoryPortMock.getNotificationConfigByUserUUID(userUuid)).thenReturn(Optional.of(config));

        notifierService.notifyClient(event);

        verify(emailServiceMock, times(1)).sendEmail(config.getEmails(), event);
        verify(androidServiceMock, never()).sendNotification(any(), any());
    }

    @Test
    public void notifyClient_shouldSendBothNotificationAndEmail_whenBothConfigsExist() {
        when(notificationConfigRepositoryPortMock.getNotificationConfigByUserUUID(userUuid)).thenReturn(Optional.of(config));

        notifierService.notifyClient(event);

        verify(androidServiceMock, times(1)).sendNotification(config.getAndroids(), event);
        verify(emailServiceMock, times(1)).sendEmail(config.getEmails(), event);
    }

    @Test
    @Timeout(1)
    public void eventFinishedNotifyClient_shouldSendMessage() throws InterruptedException {
        CompletableFuture<Void> emailFuture = CompletableFuture.runAsync(() -> {});
        CompletableFuture<Void> androidFuture = CompletableFuture.runAsync(() -> {});

        when(notificationConfigRepositoryPortMock.getNotificationConfigByUserUUID(userUuid)).thenReturn(Optional.of(config));
        when(emailServiceMock.sendEmail(any(), any())).thenReturn(emailFuture);
        when(androidServiceMock.sendNotification(any(), any())).thenReturn(androidFuture);

        var countDownLatch = new CountDownLatch(1);

        doAnswer((answer) -> {
            countDownLatch.countDown();
            return null;
        }).when(messengerOutputPortMock).eventFinishedNotificationSent(event);

        notifierService.eventFinishedNotifyClient(event);

        countDownLatch.await();
        verify(messengerOutputPortMock, times(1)).eventFinishedNotificationSent(event);
    }

}
