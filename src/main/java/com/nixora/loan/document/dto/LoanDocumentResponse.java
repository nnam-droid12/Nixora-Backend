package com.nixora.loan.document.dto;

import com.nixora.loan.document.extractor.LoanApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoanDocumentResponse {
    private UUID documentId;
    private UUID loanId;
    private String documentUrl;
    private LoanApiResponse loanData;
    private String status;
    private String documentName;
}
