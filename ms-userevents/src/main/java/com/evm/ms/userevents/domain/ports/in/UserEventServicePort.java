package com.evm.ms.userevents.domain.ports.in;

import com.evm.ms.userevents.domain.Event;

public interface UserEventServicePort {

    void addNewEvent(Event event);

}
