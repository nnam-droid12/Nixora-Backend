package com.nixora.loan.document.entities;

import java.util.regex.Pattern;

public enum LmaFieldDefinition {

    MARGIN(
            "interestPricing.margin",
            Pattern.compile("(?i)(margin)\\s*(shall be|is|:)\\s*([0-9]+(\\.[0-9]+)?\\s*(%|per cent))"),
            3
    ),

    BENCHMARK(
            "interestPricing.benchmark",
            Pattern.compile("(?i)(EURIBOR|SOFR|SONIA)"),
            1
    ),

    MATURITY_DATE(
            "dates.maturityDate",
            Pattern.compile("(?i)(maturity date)\\s*(is|:)\\s*([A-Za-z0-9 ,]+)"),
            3
    );

    public final String fieldKey;
    public final Pattern pattern;
    public final int valueGroup;

    LmaFieldDefinition(String fieldKey, Pattern pattern, int valueGroup) {
        this.fieldKey = fieldKey;
        this.pattern = pattern;
        this.valueGroup = valueGroup;
    }
}
