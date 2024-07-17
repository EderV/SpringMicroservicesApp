package com.evm.ms.notifier;

import com.evm.ms.notifier.application.notifier.AndroidService;
import com.evm.ms.notifier.application.notifier.EmailService;
import com.evm.ms.notifier.application.notifier.NotifierService;
import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.config.AndroidConfig;
import com.evm.ms.notifier.domain.config.EmailConfig;
import com.evm.ms.notifier.domain.config.NotificationConfig;
import com.evm.ms.notifier.domain.ports.out.NotificationConfigRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotifierServiceTest {

    @Mock
    private NotificationConfigRepositoryPort notificationConfigRepositoryPort;

    @Mock
    private AndroidService androidService;

    @Mock
    private EmailService emailService;

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

        verify(notificationConfigRepositoryPort, never()).getNotificationConfigByUserUUID(any());
        verify(androidService, never()).sendNotification(any(), any());
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    public void notifyClient_shouldLogWarning_whenNoNotificationConfigExists() {
        when(notificationConfigRepositoryPort.getNotificationConfigByUserUUID(userUuid)).thenReturn(Optional.empty());

        notifierService.notifyClient(event);

        verify(androidService, never()).sendNotification(any(), any());
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    public void notifyClient_shouldSendNotification_whenAndroidConfigsExist() {
        config.setEmails(List.of());
        when(notificationConfigRepositoryPort.getNotificationConfigByUserUUID(userUuid)).thenReturn(Optional.of(config));

        notifierService.notifyClient(event);

        verify(androidService, times(1)).sendNotification(config.getAndroids(), event);
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    public void notifyClient_shouldSendEmail_whenEmailConfigsExist() {
        config.setAndroids(List.of());
        when(notificationConfigRepositoryPort.getNotificationConfigByUserUUID(userUuid)).thenReturn(Optional.of(config));

        notifierService.notifyClient(event);

        verify(emailService, times(1)).sendEmail(config.getEmails(), event);
        verify(androidService, never()).sendNotification(any(), any());
    }

    @Test
    public void notifyClient_shouldSendBothNotificationAndEmail_whenBothConfigsExist() {
        when(notificationConfigRepositoryPort.getNotificationConfigByUserUUID(userUuid)).thenReturn(Optional.of(config));

        notifierService.notifyClient(event);

        verify(androidService, times(1)).sendNotification(config.getAndroids(), event);
        verify(emailService, times(1)).sendEmail(config.getEmails(), event);
    }

}
