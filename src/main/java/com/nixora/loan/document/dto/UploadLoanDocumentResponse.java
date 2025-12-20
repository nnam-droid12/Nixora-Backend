package com.nixora.loan.document.dto;

import com.nixora.loan.document.entities.DocumentStatus;

import java.util.UUID;

public record UploadLoanDocumentResponse(
        UUID documentId,
        DocumentStatus status,
        String message,
        String extractedText
) {}


