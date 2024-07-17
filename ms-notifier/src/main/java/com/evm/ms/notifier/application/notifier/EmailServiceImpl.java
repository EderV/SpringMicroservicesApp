package com.evm.ms.notifier.application.notifier;

import com.evm.ms.notifier.domain.EmailData;
import com.evm.ms.notifier.domain.Event;
import com.evm.ms.notifier.domain.config.EmailConfig;
import com.evm.ms.notifier.domain.ports.out.NotifierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final NotifierPort<EmailData> notifierPort;

    @Override
    public CompletableFuture<Void> sendEmail(List<EmailConfig> emailConfigs, Event event) {
        var emailSendRunnable = new Runnable() {
            @Override
            public void run() {
                for (EmailConfig emailConfig : emailConfigs) {
                    var subject = event.getTitle();
                    var body = event.getDescription();
                    var sendTo = emailConfig.getEmail();

                    var emailData = new EmailData(null, sendTo, subject, body);
                    var sent = notifierPort.sendNotification(emailData);
                    if (!sent) {
                        // TODO handle error when send fails
                    }
                }

            }
        };

        return CompletableFuture.runAsync(emailSendRunnable);
    }

}
