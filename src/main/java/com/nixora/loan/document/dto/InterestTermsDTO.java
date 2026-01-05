package com.nixora.loan.document.dto;

import lombok.Data;

@Data
public class InterestTermsDTO {
    private String benchmark;
    private String margin;
    private String creditAdjustmentSpread;
    private String interestPeriod;
    private String interestPaymentDate;
    private String breakCosts;
    private String defaultInterest;
    private String dayCountFraction;
    private String compoundingMethod;
    private String rollover;
}

