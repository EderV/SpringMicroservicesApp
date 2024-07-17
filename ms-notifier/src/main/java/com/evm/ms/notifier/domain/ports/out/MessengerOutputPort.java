package com.evm.ms.notifier.domain.ports.out;

import com.evm.ms.notifier.domain.Event;

public interface MessengerOutputPort {

    void eventFinishedNotificationSent(Event event);

}
