package com.nixora.loan.LoanQueryEngine.dto;

import java.time.LocalDate;
import java.util.UUID;

public record LoanSnapshotDTO(
        UUID loanId,
        String borrower,
        String facilityAgent,
        String facilityType,
        String currency,
        Double margin,
        String benchmark,
        String interestPeriod,
        LocalDate agreementDate,
        LocalDate maturityDate,
        Boolean prepaymentAllowed,
        String transferRestrictions,
        Integer covenantCount,
        Integer defaultCount,
        String governingLaw,
        String jurisdiction
) {}

