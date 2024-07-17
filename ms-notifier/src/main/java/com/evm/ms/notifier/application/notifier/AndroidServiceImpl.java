package com.evm.ms.notifier.application.notifier;

import com.evm.ms.notifier.domain.AndroidData;
import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.config.AndroidConfig;
import com.evm.ms.notifier.domain.ports.out.NotifierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AndroidServiceImpl implements AndroidService {

    private final NotifierPort<AndroidData> notifierPort;

    @Override
    public CompletableFuture<Void> sendNotification(List<AndroidConfig> androidConfigs, Event event) {
        var androidNotificationRunnable = new Runnable() {
            @Override
            public void run() {
                for (AndroidConfig androidConfig : androidConfigs) {
                    var token = androidConfig.getToken();
                    var topic = androidConfig.getIdentifier();
                    var title = event.getTitle();
                    var description = event.getDescription();

                    var androidData = new AndroidData(token, topic, title, description);
                    var sent = notifierPort.sendNotification(androidData);
                    if (!sent) {
                        // TODO handle error when send fails
                    }
                }
            }
        };

        return CompletableFuture.runAsync(androidNotificationRunnable);
    }

}
