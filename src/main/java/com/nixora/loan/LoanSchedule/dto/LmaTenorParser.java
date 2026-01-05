package com.nixora.loan.LoanSchedule.dto;

public class LmaTenorParser {

    public static Integer parseMonths(String s) {
        if (s == null) return null;

        s = s.toLowerCase();

        if (s.contains("year")) {
            int n = extractNumber(s);
            return n * 12;
        }

        if (s.contains("month")) {
            return extractNumber(s);
        }

        return null;
    }

    private static int extractNumber(String s) {
        var m = java.util.regex.Pattern.compile("(\\d+)").matcher(s);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }
}

