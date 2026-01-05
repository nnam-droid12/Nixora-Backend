package com.nixora.loan.document.dto;

import lombok.Data;

import java.util.List;

@Data
public class CovenantsDTO {

    private List<String> financial;
    private List<String> information;
    private List<String> general;

    private List<String> negativePledge;
    private List<String> disposals;
    private List<String> mergers;
    private List<String> changeOfBusiness;
    private List<String> indebtedness;
    private List<String> guarantees;
}
