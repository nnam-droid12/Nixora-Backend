package com.nixora.loan.document.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.document.dto.DeleteLoanResponse;
import com.nixora.loan.document.dto.LoanDocumentResponse;
import com.nixora.loan.document.dto.UploadLoanDocumentResponse;
import com.nixora.loan.document.entities.UpdateLoanFieldRequest;
import com.nixora.loan.document.service.LoanDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans/documents")
@RequiredArgsConstructor
public class LoanDocumentController {

    private final LoanDocumentService loanDocumentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadLoanDocumentResponse> upload(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user
    ) throws Exception {
        return ResponseEntity.ok(
                loanDocumentService.uploadDocument(file, user)
        );
    }


    @GetMapping("/getAllLoan")
    public ResponseEntity<List<LoanDocumentResponse>> getAllLoans(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                loanDocumentService.getAllLoans(user)
        );
    }

    @GetMapping("/getLoan/{loanId}")
    public ResponseEntity<LoanDocumentResponse> getLoanById(
            @PathVariable UUID loanId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                loanDocumentService.getLoanById(loanId, user)
        );
    }

    @DeleteMapping("/delete-loan/{loanId}")
    public ResponseEntity<DeleteLoanResponse> deleteLoan(
            @PathVariable UUID loanId,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                loanDocumentService.deleteLoan(loanId, user)
        );
    }

    @PatchMapping("/update-field/{loanId}")
    public ResponseEntity<?> updateField(
            @PathVariable UUID loanId,
            @RequestBody UpdateLoanFieldRequest req,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                loanDocumentService.updateField(loanId, user, req)
        );
    }


}

