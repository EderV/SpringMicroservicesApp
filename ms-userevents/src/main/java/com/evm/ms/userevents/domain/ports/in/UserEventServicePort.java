package com.evm.ms.userevents.domain.ports.in;

import com.evm.ms.userevents.domain.Event;

import java.util.List;

public interface UserEventServicePort {

    void addNewEvent(Event event);

    Event findEventById(String id);

    List<Event> getAllEvents();

    List<Event> findAllEventsByUserId(String userId);

    boolean updateEvent(Event event);

}
