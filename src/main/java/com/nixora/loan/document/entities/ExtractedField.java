package com.nixora.loan.document.entities;

import lombok.Data;

@Data
public class ExtractedField<T> {
    private T value;
    private Double confidence;
}

