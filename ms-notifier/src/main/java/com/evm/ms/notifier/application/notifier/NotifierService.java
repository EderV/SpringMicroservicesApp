package com.evm.ms.notifier.application.notifier;

import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.ports.in.NotifierServicePort;
import org.springframework.stereotype.Service;

@Service
public class NotifierService implements NotifierServicePort {

    @Override
    public void notifyClient(Event event) {

    }

}
