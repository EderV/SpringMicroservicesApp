package com.evm.ms.notifier.application.notifier;

import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.config.NotificationConfig;
import com.evm.ms.notifier.domain.ports.in.NotifierServicePort;
import com.evm.ms.notifier.domain.ports.out.MessengerOutputPort;
import com.evm.ms.notifier.domain.ports.out.NotificationConfigRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifierService implements NotifierServicePort {

    private final NotificationConfigRepositoryPort notificationConfigRepositoryPort;
    private final MessengerOutputPort messengerOutputPort;

    private final AndroidService androidService;
    private final EmailService emailService;

    @Override
    public void notifyClient(Event event) {
        var config = this.getNotificationConfig(event.getUserId());
        if (config == null) {
            log.warn("No config available for user {}. Skip notifications", event.getUserId());
            return;
        }

        this.notify(config, event);
    }

    @Override
    public void eventFinishedNotifyClient(Event event) {
        var config = this.getNotificationConfig(event.getUserId());
        if (config == null) {
            log.warn("No config available for user {}. Skip notifications", event.getUserId());
            return;
        }

        var completableFutures = this.notify(config, event);

        CompletableFuture.allOf(completableFutures)
                .thenRun(() -> messengerOutputPort.eventFinishedNotificationSent(event))
                .exceptionally(throwable -> {
                    log.error("Error while waiting notifiers", throwable);
                    return null;
                });
    }

    private CompletableFuture<?>[] notify(NotificationConfig config, Event event) {
        var tasks = new ArrayList<CompletableFuture<Void>>();

        var androidConfigs = config.getAndroids();
        if (!androidConfigs.isEmpty()) {
            tasks.add(androidService.sendNotification(androidConfigs, event));
        }

        var emailConfigs = config.getEmails();
        if (!emailConfigs.isEmpty()) {
            tasks.add(emailService.sendEmail(emailConfigs, event));
        }

        return tasks.toArray(new CompletableFuture[0]);
    }

    private NotificationConfig getNotificationConfig(String uuid) {
        final UUID userUuid;
        try {
            userUuid = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            log.error("Error parsing userUuid. Event#getUserId(): {}. Error: {}", uuid, e.getMessage());
            return null;
        }

        var optionalConfig = notificationConfigRepositoryPort.getNotificationConfigByUserUUID(userUuid);
        if (optionalConfig.isEmpty()) {
            log.warn("There is no notification config for user with uuid: {}", userUuid);
            return null;
        }

        return optionalConfig.get();
    }

}
