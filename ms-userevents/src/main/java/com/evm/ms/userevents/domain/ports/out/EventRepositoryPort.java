package com.evm.ms.userevents.domain.ports.out;

import com.evm.ms.userevents.domain.Event;

public interface EventRepositoryPort {

    void saveEvent(Event event);

}
