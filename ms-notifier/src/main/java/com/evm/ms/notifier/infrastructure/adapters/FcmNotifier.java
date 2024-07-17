package com.evm.ms.notifier.infrastructure.adapters;

import com.evm.ms.notifier.domain.AndroidData;
import com.evm.ms.notifier.domain.ports.out.NotifierPort;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("fcm-notifier")
public class FcmNotifier implements NotifierPort<AndroidData> {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public boolean sendNotification(AndroidData notification) {
        Message message = Message.builder()
            .setToken(notification.token())
            .setTopic(notification.topic())
            .putData("title", notification.title())
            .putData("description", notification.description())
            .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Error while sending fcm notification", e);
            return false;
        }

        return true;
    }

}
