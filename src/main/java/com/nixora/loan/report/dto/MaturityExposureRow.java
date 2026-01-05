package com.nixora.loan.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MaturityExposureRow {
    private int year;
    private long loanCount;
}

