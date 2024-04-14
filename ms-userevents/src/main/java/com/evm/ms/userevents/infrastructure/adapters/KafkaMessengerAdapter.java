package com.evm.ms.userevents.infrastructure.adapters;

import com.evm.ms.userevents.domain.ports.out.MessageBrokerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessengerAdapter implements MessageBrokerPort<String> {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message).whenComplete((res, ex) -> {
            if (ex != null) {
                log.error("Something went wrong sending message {" + message + "} to topic {" + topic + "}");
            }
        });
    }

}
