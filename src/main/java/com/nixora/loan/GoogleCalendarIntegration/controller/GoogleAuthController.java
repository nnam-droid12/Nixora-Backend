package com.nixora.loan.GoogleCalendarIntegration.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.GoogleCalendarIntegration.services.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/google")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final GoogleOAuthService oauth;

    @GetMapping("/connect")
    public ResponseEntity<?> connect(Authentication auth) {
        User user = (User) auth.getPrincipal();

        String state = user.getUserId().toString();   // simple and safe
        return ResponseEntity.ok(Map.of(
                "url", oauth.authUrl(state)
        ));
    }


    @GetMapping("/callback")
    public String callback(@RequestParam String code,
                           @RequestParam String state) throws Exception {

        Integer userId = Integer.valueOf(state);
        oauth.exchange(code, userId);

        return "Google Calendar Connected. You can close this tab.";
    }

}
