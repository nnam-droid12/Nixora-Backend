package com.nixora.loan.LoanSchedule.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter @Setter
public class LoanScheduleEvent {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID loanId;

    private LocalDate eventDate;

    @Enumerated(EnumType.STRING)
    private ScheduleEventType type;

    @Column(columnDefinition = "TEXT")
    private String description;
}

