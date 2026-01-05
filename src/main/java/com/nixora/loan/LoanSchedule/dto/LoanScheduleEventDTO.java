package com.nixora.loan.LoanSchedule.dto;


import java.time.LocalDate;

public record LoanScheduleEventDTO(
        LocalDate date,
        String type,
        String description
) {}
