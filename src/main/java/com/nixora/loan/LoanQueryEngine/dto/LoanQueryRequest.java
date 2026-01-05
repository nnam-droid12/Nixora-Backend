package com.nixora.loan.LoanQueryEngine.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LoanQueryRequest {

    private Filters filters;
    private List<String> keywords;

    @Data
    public static class Filters {
        private String governingLaw;
        private String jurisdiction;
        private String benchmark;
        private String facilityType;
        private String currency;
        private String borrower;
        private String facilityAgent;
        private Boolean transferableOnly;
        private Boolean prepaymentAllowed;
        private Integer maxMonthsToMaturity;
        private LocalDate agreementAfter;
        private Double minMargin;
        private Double maxMargin;
    }
}
