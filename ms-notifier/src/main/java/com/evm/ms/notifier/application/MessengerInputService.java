package com.evm.ms.notifier.application;

import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.ports.in.MessengerInputPort;
import com.evm.ms.notifier.domain.ports.in.NotifierServicePort;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessengerInputService implements MessengerInputPort {

    private final Gson gson;
    private final NotifierServicePort notifierServicePort;

    @Override
    public void eventNoticeReceived(String message) {
        var event = this.toEvent(message);
        if (event != null) {
            notifierServicePort.notifyClient(event);
        }
    }

    @Override
    public void eventFinishedReceived(String message) {
        var event = this.toEvent(message);
        if (event != null) {
            notifierServicePort.eventFinishedNotifyClient(event);
        }
    }

    private Event toEvent(String message) {
        final Event event;
        try {
            event = gson.fromJson(message, Event.class);
        } catch (JsonSyntaxException e) {
            log.error("Error decoding json. JSON: {}. Error: {}", message, e.getMessage());
            return null;
        }

        return event;
    }

}
