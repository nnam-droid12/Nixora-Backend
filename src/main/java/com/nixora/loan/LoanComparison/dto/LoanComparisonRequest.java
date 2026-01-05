package com.nixora.loan.LoanComparison.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class LoanComparisonRequest {
    private List<UUID> loanIds;
}

