package com.evm.ms.scheduler.domain.ports;

public interface MessengerInputPort {

    void newEventReceived(String message);

    void updatedEventReceived(String message);

}
