package com.nixora.loan.document.dto;

import lombok.Data;

@Data
public class FinancialCovenant {

    private String ratioType;
    private String threshold;

    private String testDate;

    private String level;

    private String cureRights;
}
