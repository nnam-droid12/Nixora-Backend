package com.nixora.loan.document.dto;

import lombok.Data;

@Data
public class RepaymentTermsDTO {

    private String repaymentDate;
    private String repaymentInstalments;
    private String amortisationSchedule;
    private String balloonPayment;

    private Boolean voluntaryPrepayment;
    private Boolean mandatoryPrepayment;
    private Boolean changeOfControlPrepayment;
    private Boolean illegalityPrepayment;
    private Boolean taxGrossUpPrepayment;
}
