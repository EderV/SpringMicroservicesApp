package com.evm.ms.notifier.domain.ports.in;

import com.evm.ms.notifier.domain.Event;

public interface NotifierServicePort {

    void notifyClient(Event event);

}
