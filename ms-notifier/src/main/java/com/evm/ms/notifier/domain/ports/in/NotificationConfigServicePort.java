package com.evm.ms.notifier.domain.ports.in;

import com.evm.ms.notifier.domain.config.NotificationConfig;

public interface NotificationConfigServicePort {

    void saveNotificationConfig(NotificationConfig notificationConfig);

}
