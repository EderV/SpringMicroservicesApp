package com.evm.ms.scheduler.domain.ports;

import com.evm.ms.scheduler.domain.Event;

public interface MessengerOutputPort {

    void sendEventNotice(Event event);
    void sendEventFinished(Event event);

}
