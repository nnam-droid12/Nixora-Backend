package com.nixora.loan.PushNotification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.nixora.auth.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DependsOn("firebaseConfig")
public class PushNotificationService {

    public void send(User user, String title, String body) {
        if (user.getFcmToken() == null || user.getFcmToken().isBlank()) {
            log.warn("User {} has no FCM token. Skipping notification.", user.getEmail());
            return;
        }

        if (FirebaseApp.getApps().isEmpty()) {
            log.error("Cannot send notification: FirebaseApp is not initialized!");
            return;
        }

        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent message to {}. FCM Response: {}", user.getEmail(), response);
        } catch (Exception e) {
            log.error("Error sending FCM message to user {}: {}", user.getEmail(), e.getMessage());
        }
    }
}