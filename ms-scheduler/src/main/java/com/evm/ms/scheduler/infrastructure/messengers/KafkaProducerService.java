package com.evm.ms.scheduler.infrastructure.messengers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message).whenComplete((res, ex) -> {
            if (ex != null) {
                log.error("Something went wrong sending message {{}} to topic {{}}", message, topic);
            }
        });
    }

}
