package com.nixora.loan.document.dto;

import lombok.Data;

@Data
public class TransferProvisionsDTO {

    private Boolean qualifyingLender;
    private String transferCertificate;
    private String assignmentAgreement;
    private String minimumHolding;
    private String whiteList;
    private String blackList;
    private Boolean defaultingLender;
    private Boolean yankTheBank;
}
