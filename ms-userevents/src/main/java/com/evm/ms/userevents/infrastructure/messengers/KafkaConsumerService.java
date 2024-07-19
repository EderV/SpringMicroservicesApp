package com.evm.ms.userevents.infrastructure.messengers;

import com.evm.ms.userevents.domain.MessageTopicsConstants;
import com.evm.ms.userevents.domain.ports.in.MessengerInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MessengerInputPort messengerInputPort;

    @KafkaListener(topics = MessageTopicsConstants.EVENT_FINISHED_NOTIFICATION_SENT)
    public void eventFinished(String message) {
        log.error("Event finished received: {}", message);

        messengerInputPort.eventFinished(message);
    }

}
