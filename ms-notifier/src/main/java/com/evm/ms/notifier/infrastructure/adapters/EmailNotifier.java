package com.evm.ms.notifier.infrastructure.adapters;

import com.evm.ms.notifier.domain.EmailData;
import com.evm.ms.notifier.domain.ports.out.NotifierPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("email-notifier")
public class EmailNotifier implements NotifierPort<EmailData> {

    private final JavaMailSender mailSender;

    @Override
    public boolean sendNotification(EmailData notification) {
        log.info("Sending email notification. EmailData: {}", notification);

        var from = notification.from();
        if (from == null || from.isEmpty()) {
            from = "noreply@notifierapp.com";
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(notification.to());
        message.setSubject(notification.subject());
        message.setText(notification.body());

        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Error sending email", e);
            return false;
        }

        return true;
    }

}

