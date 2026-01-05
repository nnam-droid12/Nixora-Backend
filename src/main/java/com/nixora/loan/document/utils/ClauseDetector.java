package com.nixora.loan.document.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClauseDetector {

    private static final Pattern CLAUSE_PATTERN =
            Pattern.compile("(?m)^(\\d+(?:\\.\\d+)*(?:\\([a-z]\\))?)");

    public static String findClause(String text, int offset) {
        String before = text.substring(0, offset);
        Matcher matcher = CLAUSE_PATTERN.matcher(before);

        String clause = null;
        while (matcher.find()) {
            clause = matcher.group(1);
        }

        return clause != null ? clause : "UNKNOWN";
    }
}
