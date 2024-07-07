package com.evm.ms.notifier.domain.ports.out;

import com.evm.ms.notifier.domain.config.NotificationConfig;

public interface NotificationConfigRepositoryPort {

    void saveNotificationConfig(NotificationConfig notificationConfig);

}
