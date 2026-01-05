package com.nixora.loan.document.dto;

import lombok.Data;

@Data
public class UtilisationTerms {

    private String utilisationRequest;
    private String utilisationDate;
    private String utilisationAmount;
    private String utilisationCurrency;
    private String utilisationNotice;
    private String minimumAmount;
    private Boolean multipleDrawings;
    private String cancelledCommitments;
}

