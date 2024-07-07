package com.evm.ms.notifier.infrastructure.dto.request;

import com.evm.ms.notifier.infrastructure.dto.entity.NotificationConfigEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfigRequest {

    private UUID id;

    private NotificationConfigEntity notificationConfig;

    private String email;

    private UUID createdBy;

    private Date createdAt;

    private UUID updatedBy;

    private Date updatedAt;

}
