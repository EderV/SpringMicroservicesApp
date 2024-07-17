package com.evm.ms.notifier.domain.ports.in;

public interface MessengerInputPort {

    void eventNoticeReceived(String message);

    void eventFinishedReceived(String message);

}
