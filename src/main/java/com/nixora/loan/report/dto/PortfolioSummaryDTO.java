package com.nixora.loan.report.dto;

import lombok.Data;

@Data
public class PortfolioSummaryDTO {
    private long totalLoans;
    private double averageMargin;
    private long highRiskLoans;
    private long transferableLoans;
}

