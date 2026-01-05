package com.nixora.loan.GoogleCalendarIntegration.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CalendarEventDTO {
    private String type;
    private String description;
    private LocalDate date;
}
