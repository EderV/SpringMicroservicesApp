package com.evm.ms.notifier.application.notifier;

import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.config.EmailConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EmailService {

    CompletableFuture<Void> sendEmail(List<EmailConfig> emailConfigs, Event event);

}

