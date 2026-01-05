package com.nixora.loan.PushNotification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.nixora.auth.entities.User;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    public void send(User user, String title, String body) {
        if (user.getFcmToken() == null) return;

        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
