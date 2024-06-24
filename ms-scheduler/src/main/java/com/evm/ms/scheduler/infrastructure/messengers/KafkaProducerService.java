package com.evm.ms.scheduler.infrastructure.messengers;

import com.evm.ms.scheduler.domain.Event;
import com.evm.ms.scheduler.domain.MessageTopicsConstants;
import com.evm.ms.scheduler.domain.ports.MessengerOutputPort;
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
    public void sendEventNotice(Event event) {
        var message = gson.toJson(event);

        log.info("Sending event notice message to Kafka: {}", message);

        var topic = MessageTopicsConstants.EVENT_NOTICE;
        kafkaTemplate.send(topic, message).whenComplete((res, ex) -> {
            if (ex != null) {
                handleGenericError(message, topic);
            }
        });
    }

    @Override
    public void sendEventFinished(Event event) {
        var message = gson.toJson(event);

        log.info("Sending event finished message to Kafka: {}", message);

        var topic = MessageTopicsConstants.EVENT_FINISHED;
        kafkaTemplate.send(topic, message).whenComplete((res, ex) -> {
            if (ex != null) {
                handleGenericError(message, topic);
            }
        });
    }

    private void handleGenericError(String message, String topic) {
        log.error("Something went wrong sending message {{}} to topic {{}}", message, topic);
    }

}
