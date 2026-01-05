package com.nixora.loan.LoanComparison.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.LoanComparison.dto.LoanComparisonRequest;
import com.nixora.loan.LoanComparison.dto.LoanComparisonResult;
import com.nixora.loan.LoanComparison.service.LoanComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanComparisonController {

    private final LoanComparisonService loanComparisonService;

    @PostMapping("/compare")
    public ResponseEntity<LoanComparisonResult> compare(
            @RequestBody LoanComparisonRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                loanComparisonService.compare(request.getLoanIds(), user)
        );
    }

}
