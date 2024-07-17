package com.evm.ms.notifier.application.notifier;

import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.config.AndroidConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AndroidService {

    CompletableFuture<Void> sendNotification(List<AndroidConfig> androidConfigs, Event event);

}
