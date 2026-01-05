package com.nixora.loan.document.dto;

import lombok.Data;

import java.util.List;

@Data
public class PartiesDto {
    private String borrower;
    private String parent;
    private String guarantor;

    private List<String> obligors;
    private List<String> lenders;
    private List<String> originalLenders;
    private List<String> financeParties;

    private String facilityAgent;
    private String securityAgent;
    private String arranger;
    private String bookrunner;
}
