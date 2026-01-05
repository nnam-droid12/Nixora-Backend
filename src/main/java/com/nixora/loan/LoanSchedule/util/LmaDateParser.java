package com.nixora.loan.LoanSchedule.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class LmaDateParser {

    private static final DateTimeFormatter[] formats = {
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("d MMM uuuu"),
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
    };

    public static LocalDate parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        String text = raw.trim();

        for (var f : formats) {
            try {
                return LocalDate.parse(text, f);
            } catch (Exception ignored) {}
        }


        return null;
    }
}
