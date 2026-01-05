package com.nixora.loan.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class HighRiskLoanRow {
    private UUID loanId;
    private int covenantCount;
    private int defaultCount;
}
