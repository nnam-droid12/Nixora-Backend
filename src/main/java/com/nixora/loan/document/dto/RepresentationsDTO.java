package com.nixora.loan.document.dto;

import lombok.Data;

import java.util.List;

@Data
public class RepresentationsDTO {

    private List<String> bindingObligations;
    private List<String> nonConflict;
    private List<String> powerAndAuthority;
    private List<String> noDefault;
    private List<String> financialStatements;
    private List<String> litigation;
    private List<String> environmental;
    private List<String> tax;
}

