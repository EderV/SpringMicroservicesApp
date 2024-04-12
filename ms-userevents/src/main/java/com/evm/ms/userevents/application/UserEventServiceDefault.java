package com.evm.ms.userevents.application;

import com.evm.ms.userevents.domain.Event;
import com.evm.ms.userevents.domain.ports.in.UserEventServicePort;
import com.evm.ms.userevents.domain.ports.out.EventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventServiceDefault implements UserEventServicePort {

    private final EventRepositoryPort eventRepositoryPort;

    @Override
    public void addNewEvent(Event event) {
        eventRepositoryPort.saveEvent(event);
    }

}
