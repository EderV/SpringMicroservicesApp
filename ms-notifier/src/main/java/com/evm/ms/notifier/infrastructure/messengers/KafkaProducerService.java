package com.evm.ms.notifier.infrastructure.messengers;

import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.MessageTopicsConstants;
import com.evm.ms.notifier.domain.ports.out.MessengerOutputPort;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService implements MessengerOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @Override
    public void eventFinishedNotificationSent(Event event) {
        var message = gson.toJson(event);

        log.info("Event {{}} finished. Notification sent to client", event.getTitle());

        var topic = MessageTopicsConstants.EVENT_FINISHED_NOTIFICATION_SENT;
        kafkaTemplate.send(topic, message).whenComplete((res, ex) -> {
            if (ex != null) {
                log.error("Something went wrong sending message {{}} to topic {{}}", message, topic);
            }
        });
    }

}
