package com.evm.ms.scheduler.application.adapters;

import com.evm.ms.scheduler.application.scheduler.EventScheduler;
import com.evm.ms.scheduler.domain.Event;
import com.evm.ms.scheduler.domain.ports.MessengerInputPort;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessengerInputAdapter implements MessengerInputPort {

    private final Gson gson;
    private final EventScheduler eventScheduler;

    @Override
    public void newEventReceived(String message) {
        var event = parseToObject(message);
        if (event != null) {
            eventScheduler.scheduleEvent(event);
        }
    }

    @Override
    public void updatedEventReceived(String message) {
        var event = parseToObject(message);
        if (event != null) {
            eventScheduler.modifyScheduledEvent(event);
        }
    }

    private Event parseToObject(String message) {
        Event event = null;
        try {
            event = gson.fromJson(message, Event.class);
        } catch (JsonSyntaxException ex) {
            log.error("Error while parsing message to Event object", ex);
        }

        return event;
    }

}
