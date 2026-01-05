package com.nixora.loan.collaborate.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.collaborate.service.CollaborationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/collaboration")
@RequiredArgsConstructor
public class CollaborationController {

    private final CollaborationService collaborationService;


    @PostMapping("/invite/{loanId}")
    public ResponseEntity<Map<String, String>> invite(
            @PathVariable UUID loanId,
            @AuthenticationPrincipal User user
    ) {
        String url = collaborationService.createInvite(user, loanId);
        return ResponseEntity.ok(Map.of("inviteUrl", url));
    }


    @PostMapping("/accept")
    public ResponseEntity<?> accept(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal User user
    ) {
        String token = body.get("token");

        collaborationService.acceptInvite(token, user);

        return ResponseEntity.ok(Map.of("status", "collaboration granted"));
    }
}
