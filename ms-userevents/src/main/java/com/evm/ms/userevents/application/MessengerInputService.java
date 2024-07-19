package com.evm.ms.userevents.application;

import com.evm.ms.userevents.domain.Event;
import com.evm.ms.userevents.domain.ports.in.MessengerInputPort;
import com.evm.ms.userevents.domain.ports.out.EventRepositoryPort;
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
    private final EventRepositoryPort eventRepositoryPort;

    @Override
    public void eventFinished(String message) {
        final Event event;
        try {
            event = gson.fromJson(message, Event.class);
        } catch (JsonSyntaxException e) {
            log.error("Error decoding json. JSON: {}. Error: {}", message, e.getMessage());
            return;
        }

        eventRepositoryPort.updateEventTriggered(event.getId(), true);
    }

}
