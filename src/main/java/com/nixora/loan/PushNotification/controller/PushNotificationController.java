package com.nixora.loan.PushNotification.controller;

import com.nixora.auth.entities.User;
import com.nixora.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class PushNotificationController {

    private final UserRepository userRepo;

    @PostMapping("/register-device")
    public void register(@RequestBody Map<String, String> body,
                         @AuthenticationPrincipal User user) {

        user.setFcmToken(body.get("token"));
        userRepo.save(user);
    }
}
