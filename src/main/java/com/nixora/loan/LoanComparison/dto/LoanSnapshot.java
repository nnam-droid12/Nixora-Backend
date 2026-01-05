package com.nixora.loan.LoanComparison.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
public class LoanSnapshot {
    private UUID loanId;
    private String margin;
    private String benchmark;
    private String maturity;
    private Boolean prepaymentAllowed;
    private String transferRestrictions;
    private Integer covenantCount;
    private Integer defaultCount;
    private String governingLaw;

    private String utilisationDate;
    private Integer tenorMonths;

}
