package com.evm.ms.notifier.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationConfigRequest {

    private UUID id;

    private UUID userId;

    private String category;

    private List<EmailConfigRequest> emails;

    private List<AndroidConfigRequest> androids;

    private UUID createdBy;

    private Date createdAt;

    private UUID updatedBy;

    private Date updatedAt;

}
