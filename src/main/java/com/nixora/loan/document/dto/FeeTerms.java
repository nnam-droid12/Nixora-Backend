package com.nixora.loan.document.dto;

import lombok.Data;

@Data
public class FeeTerms {

    private String arrangementFee;
    private String participationFee;
    private String commitmentFee;
    private String utilisationFee;
    private String agencyFee;
    private String cancellationFee;
    private String frontEndFee;
}

