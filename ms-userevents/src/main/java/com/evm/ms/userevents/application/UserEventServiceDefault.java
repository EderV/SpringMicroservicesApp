package com.evm.ms.userevents.application;

import com.evm.ms.userevents.domain.Event;
import com.evm.ms.userevents.domain.ports.in.UserEventServicePort;
import com.evm.ms.userevents.domain.ports.out.EventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEventServiceDefault implements UserEventServicePort {

    private final EventRepositoryPort eventRepositoryPort;

    @Override
    public void addNewEvent(Event event) {
        eventRepositoryPort.saveEvent(event);
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
        return eventRepositoryPort.updateEventOnlyNonNulls(event);
    }

}
