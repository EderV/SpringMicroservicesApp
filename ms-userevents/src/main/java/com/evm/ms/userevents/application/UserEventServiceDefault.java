package com.evm.ms.userevents.application;

import com.evm.ms.userevents.domain.Event;
import com.evm.ms.userevents.domain.MessageTopicsConstants;
import com.evm.ms.userevents.domain.ports.in.UserEventServicePort;
import com.evm.ms.userevents.domain.ports.out.EventRepositoryPort;
import com.evm.ms.userevents.domain.ports.out.MessageBrokerPort;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEventServiceDefault implements UserEventServicePort {

    private final Gson gson;
    private final EventRepositoryPort eventRepositoryPort;
    private final MessageBrokerPort<String> messageBrokerPort;

    @Override
    public void addNewEvent(Event event) {
        var savedEvent = eventRepositoryPort.saveEvent(event);

        messageBrokerPort.sendMessage(MessageTopicsConstants.NEW_EVENT, gson.toJson(savedEvent));
    }

    @Override
    public Event findEventById(String id) {
        return eventRepositoryPort.findById(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepositoryPort.findAll();
    }

    @Override
    public List<Event> findAllEventsByUserId(String userId) {
        return eventRepositoryPort.findAllByUserId(userId);
    }

    @Override
    public boolean updateEvent(Event event) {
        var updatedEvent = eventRepositoryPort.updateEventOnlyNonNulls(event);
        if (updatedEvent == null) {
            return false;
        }

        messageBrokerPort.sendMessage(MessageTopicsConstants.UPDATED_EVENT, gson.toJson(updatedEvent));
        return true;
    }

}
