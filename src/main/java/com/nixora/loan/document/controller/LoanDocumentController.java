package com.nixora.loan.document.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.document.dto.UploadLoanDocumentResponse;
import com.nixora.loan.document.service.LoanDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/loans/documents")
@RequiredArgsConstructor
public class LoanDocumentController {

    private final LoanDocumentService loanDocumentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadLoanDocumentResponse> upload(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                loanDocumentService.uploadDocument(file, user)
        );
    }
}
