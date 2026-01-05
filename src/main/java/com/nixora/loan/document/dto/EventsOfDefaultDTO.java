package com.nixora.loan.document.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventsOfDefaultDTO {
    private List<String> nonPayment;
    private List<String> financialCovenantBreach;
    private List<String> otherObligationBreach;
    private List<String> misrepresentation;
    private List<String> crossDefault;
    private List<String> insolvency;
    private List<String> creditorsProcess;
    private List<String> unlawfulness;
    private List<String> changeOfControl;
    private List<String> repudiation;
    private List<String> auditQualification;
}

