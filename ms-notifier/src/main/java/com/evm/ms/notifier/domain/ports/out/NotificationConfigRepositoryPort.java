package com.evm.ms.notifier.domain.ports.out;

import com.evm.ms.notifier.domain.config.NotificationConfig;

import java.util.Optional;
import java.util.UUID;

public interface NotificationConfigRepositoryPort {

    void saveNotificationConfig(NotificationConfig notificationConfig);

    Optional<NotificationConfig> getNotificationConfigByUUID(UUID id);

    Optional<NotificationConfig> getNotificationConfigByUserUUID(UUID id);

}
