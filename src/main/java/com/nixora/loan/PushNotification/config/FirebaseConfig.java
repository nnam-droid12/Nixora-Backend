package com.nixora.loan.PushNotification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            String filePath = "/etc/secrets/firebase-service-account.json";


            File file = new File(filePath);
            InputStream is;

            if (file.exists()) {
                is = new FileInputStream(file);
                log.info("Loading Firebase key from Render secret path: {}", filePath);
            } else {

                is = getClass().getClassLoader().getResourceAsStream("firebase-service-account.json");
                log.info("Loading Firebase key from classpath resources.");
            }

            if (is == null) {
                log.error("CRITICAL: Firebase service account file not found!");
                return null;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(is))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                log.info("Initializing Firebase [DEFAULT] app...");
                return FirebaseApp.initializeApp(options);
            } else {
                return FirebaseApp.getInstance();
            }
        } catch (Exception e) {
            log.error("CRITICAL: Firebase initialization failed: {}", e.getMessage());
            return null;
        }
    }
}