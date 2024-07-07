package com.evm.ms.notifier.application;

import com.evm.ms.notifier.domain.config.NotificationConfig;
import com.evm.ms.notifier.domain.ports.in.NotificationConfigServicePort;
import com.evm.ms.notifier.domain.ports.out.NotificationConfigRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConfigService implements NotificationConfigServicePort {

    private final NotificationConfigRepositoryPort notificationConfigRepository;

    @Override
    public void saveNotificationConfig(NotificationConfig notificationConfig) {
        notificationConfigRepository.saveNotificationConfig(notificationConfig);
    }

}
