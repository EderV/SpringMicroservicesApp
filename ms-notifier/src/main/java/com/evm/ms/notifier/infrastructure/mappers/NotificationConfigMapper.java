package com.evm.ms.notifier.infrastructure.mappers;

import com.evm.ms.notifier.domain.config.AndroidConfig;
import com.evm.ms.notifier.domain.config.EmailConfig;
import com.evm.ms.notifier.domain.config.NotificationConfig;
import com.evm.ms.notifier.infrastructure.dto.entity.NotificationConfigEntity;
import com.evm.ms.notifier.infrastructure.dto.request.NotificationConfigRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationConfigMapper {

    NotificationConfigMapper MAPPER = Mappers.getMapper(NotificationConfigMapper.class);

    @Mapping(target = "androids", expression = "java(mapAndroids(notificationConfigRequest))")
    @Mapping(target = "emails", expression = "java(mapEmails(notificationConfigRequest))")
    NotificationConfig toNotificationConfig(NotificationConfigRequest notificationConfigRequest);

    NotificationConfig toNotificationConfig(NotificationConfigEntity notificationConfigEntity);

    NotificationConfigRequest toNotificationConfigRequest(NotificationConfig notificationConfig);

    NotificationConfigEntity toNotificationConfigEntity(NotificationConfig notificationConfig);

    default List<AndroidConfig> mapAndroids(NotificationConfigRequest notificationConfigRequest) {
        var notificationWithId = new NotificationConfig();
        notificationWithId.setId(notificationConfigRequest.getId());
        var androids = new ArrayList<AndroidConfig>();
        for (var androidRequest : notificationConfigRequest.getAndroids()) {
            var android = new AndroidConfig();
            android.setNotificationConfig(notificationWithId);
            android.setToken(androidRequest.getToken());
            android.setIdentifier(androidRequest.getIdentifier());
            android.setCreatedBy(androidRequest.getCreatedBy());
            android.setCreatedAt(androidRequest.getCreatedAt());
            android.setUpdatedBy(androidRequest.getUpdatedBy());
            android.setUpdatedAt(androidRequest.getUpdatedAt());
            androids.add(android);
        }
        return androids;
    }

    default List<EmailConfig> mapEmails(NotificationConfigRequest notificationConfigRequest) {
        var notificationWithId = new NotificationConfig();
        notificationWithId.setId(notificationConfigRequest.getId());
        var emails = new ArrayList<EmailConfig>();
        for (var emailRequest : notificationConfigRequest.getEmails()) {
            var email = new EmailConfig();
            email.setNotificationConfig(notificationWithId);
            email.setEmail(emailRequest.getEmail());
            email.setCreatedBy(emailRequest.getCreatedBy());
            email.setCreatedAt(emailRequest.getCreatedAt());
            email.setUpdatedBy(emailRequest.getUpdatedBy());
            email.setUpdatedAt(emailRequest.getUpdatedAt());
            emails.add(email);
        }

        return emails;
    }

}
