package com.evm.ms.userevents.domain.ports.out;

import com.evm.ms.userevents.domain.Event;

import java.util.List;

public interface EventRepositoryPort {

    List<Event> findAll();

    List<Event> findAllByUserId(String userId);

    Event findById(String id);

    Event saveEvent(Event event);

    boolean updateEventOnlyNonNulls(Event event);

}
