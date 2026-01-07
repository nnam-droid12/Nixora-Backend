package com.nixora.loan.PushNotification.controller;

import com.nixora.auth.entities.User;
import com.nixora.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class PushNotificationController {

    private final UserRepository userRepo;

    @PostMapping("/register-device")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body,
                                      @AuthenticationPrincipal User user) {

        log.info("Received registration request for user: {}", user.getEmail());
        log.info("Full request body: {}", body);

        String token = body.get("fcmToken");

        if (token == null || token.isBlank()) {
            log.warn("FCM Token is NULL or BLANK in the request body!");
        } else {
            log.info("Extracted Token: {}...", token.substring(0, Math.min(token.length(), 10)));
        }

        user.setFcmToken(token);
        userRepo.save(user);

        return ResponseEntity.ok(Map.of("status", "success", "receivedToken", token != null));
    }
}