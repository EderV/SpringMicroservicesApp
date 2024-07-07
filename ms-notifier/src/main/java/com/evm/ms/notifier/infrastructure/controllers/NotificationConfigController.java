package com.evm.ms.notifier.infrastructure.controllers;

import com.evm.ms.notifier.domain.config.NotificationConfig;
import com.evm.ms.notifier.domain.ports.in.NotificationConfigServicePort;
import com.evm.ms.notifier.infrastructure.dto.request.NotificationConfigRequest;
import com.evm.ms.notifier.infrastructure.mappers.NotificationConfigMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ms/notifier/config")
@RequiredArgsConstructor
public class NotificationConfigController {

    private final Gson gson;

    private final NotificationConfigServicePort notificationConfigServicePort;

    @PostMapping
    public ResponseEntity<?> modifyConfig(@RequestBody NotificationConfigRequest notificationConfigRequest)
            throws IllegalArgumentException {

        log.error(gson.toJson(notificationConfigRequest));

        var notificationConfig = toNotificationConfig(notificationConfigRequest);

        log.error(gson.toJson(notificationConfig));

        notificationConfigServicePort.saveNotificationConfig(notificationConfig);

        return ResponseEntity.ok("Notification config updated");
    }

    private NotificationConfig toNotificationConfig(NotificationConfigRequest notificationConfigRequest) {
        return NotificationConfigMapper.MAPPER.toNotificationConfig(notificationConfigRequest);
    }

}
