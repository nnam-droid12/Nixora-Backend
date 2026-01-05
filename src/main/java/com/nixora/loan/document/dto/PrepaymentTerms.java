package com.nixora.loan.document.dto;


import lombok.Data;

@Data
public class PrepaymentTerms {

    private String prepaymentType;
    private Boolean voluntary;
    private Boolean assetSale;
    private Boolean insuranceProceeds;
    private Boolean debtIssuance;
    private Boolean changeOfControl;
    private Boolean illegality;
    private Boolean tax;
    private String breakCosts;
    private String prepaymentFee;

}

