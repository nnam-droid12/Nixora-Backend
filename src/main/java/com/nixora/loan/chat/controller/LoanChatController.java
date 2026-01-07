package com.nixora.loan.chat.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.chat.dto.LoanChatRequest;
import com.nixora.loan.chat.dto.LoanChatResponse;
import com.nixora.loan.chat.service.LoanChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/loan-chat")
@RequiredArgsConstructor
public class LoanChatController {

    private final LoanChatService chatService;

    @PostMapping("/chat/{loanId}")
    public ResponseEntity<LoanChatResponse> chat(
            @PathVariable UUID loanId,
            @RequestBody LoanChatRequest request,
            @AuthenticationPrincipal User user
    ) throws Exception {

        String answer = chatService.askQuestion(loanId, user, request.getMessage());

        return ResponseEntity.ok(new LoanChatResponse(answer));
    }
}
