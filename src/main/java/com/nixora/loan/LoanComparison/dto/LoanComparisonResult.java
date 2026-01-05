package com.nixora.loan.LoanComparison.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class LoanComparisonResult {
    private List<LoanSnapshot> loans;
    private Map<String, UUID> comparison;
}
