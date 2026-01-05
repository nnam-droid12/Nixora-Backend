package com.nixora.loan.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DeleteLoanResponse {
    private UUID loanId;
    private String message;
}

