package com.evm.ms.notifier.infrastructure.messengers;

import com.evm.ms.notifier.domain.MessageTopicsConstants;
import com.evm.ms.notifier.domain.ports.in.MessengerInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MessengerInputPort messengerInputPort;

    @KafkaListener(topics = MessageTopicsConstants.EVENT_NOTICE)
    public void listenEventNotice(String message) {
        log.info("Event notice received: {{}}", message);

        messengerInputPort.eventNoticeReceived(message);
    }

    @KafkaListener(topics = MessageTopicsConstants.EVENT_FINISHED)
    public void listenEventFinished(String message) {
        log.info("Event finished received: {{}}", message);

        messengerInputPort.eventFinishedReceived(message);
    }

}
