package com.evm.ms.notifier.infrastructure.adapters;

import com.evm.ms.notifier.domain.config.NotificationConfig;
import com.evm.ms.notifier.domain.ports.out.NotificationConfigRepositoryPort;
import com.evm.ms.notifier.infrastructure.dto.entity.NotificationConfigEntity;
import com.evm.ms.notifier.infrastructure.mappers.NotificationConfigMapper;
import com.evm.ms.notifier.infrastructure.repository.NotificationConfigRepository;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConfigRepositoryAdapter implements NotificationConfigRepositoryPort {

    private final Gson gson;

    private final NotificationConfigRepository repository;

    @Override
    @Transactional
    public void saveNotificationConfig(NotificationConfig notificationConfig) {
        log.error(gson.toJson(notificationConfig));
        var notificationConfigEntity = toNotificationConfigEntity(notificationConfig);
        log.error(gson.toJson(notificationConfigEntity));

        var emails = notificationConfigEntity.getEmails();
        var androids = notificationConfigEntity.getAndroids();

        notificationConfigEntity.setEmails(null);
        notificationConfigEntity.setAndroids(null);

        emails.forEach(emailConfigEntity -> emailConfigEntity.setNotificationConfig(notificationConfigEntity));
        androids.forEach((androidConfigEntity) -> androidConfigEntity.setNotificationConfig(notificationConfigEntity));

        notificationConfigEntity.setEmails(emails);
        notificationConfigEntity.setAndroids(androids);

        repository.save(notificationConfigEntity);
    }

    private NotificationConfigEntity toNotificationConfigEntity(NotificationConfig notificationConfig) {
        return NotificationConfigMapper.MAPPER.toNotificationConfigEntity(notificationConfig);
    }

}
