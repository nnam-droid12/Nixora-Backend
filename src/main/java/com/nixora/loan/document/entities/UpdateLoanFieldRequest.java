package com.nixora.loan.document.entities;

import lombok.Data;

@Data
public class UpdateLoanFieldRequest {
    private String path;
    private String value;
}
