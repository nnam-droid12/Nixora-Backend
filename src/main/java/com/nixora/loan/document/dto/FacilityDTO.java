package com.nixora.loan.document.dto;

import lombok.Data;

@Data
public class FacilityDTO {
    private String facilityId;
    private String facilityType;
    private String facilityName;

    private String currency;
    private String facilityAmount;
    private String commitment;
    private String availabilityPeriod;
    private String drawstop;
    private String finalMaturityDate;
    private String repaymentProfile;
    private String purpose;
}

