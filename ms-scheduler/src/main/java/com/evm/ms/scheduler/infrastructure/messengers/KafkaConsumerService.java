package com.evm.ms.scheduler.infrastructure.messengers;

import com.evm.ms.scheduler.domain.MessageTopicsConstants;
import com.evm.ms.scheduler.domain.ports.MessengerInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MessengerInputPort messengerInputPort;

    @KafkaListener(topics = MessageTopicsConstants.NEW_EVENT)
    public void listenNewUserEventCreated(String message) {
        log.info("New event received: {{}}", message);

        messengerInputPort.newEventReceived(message);
    }

    @KafkaListener(topics = MessageTopicsConstants.UPDATED_EVENT)
    public void listenUserEventUpdated(String message) {
        log.info("Updated event received {{}}", message);

        messengerInputPort.updatedEventReceived(message);
    }

}
