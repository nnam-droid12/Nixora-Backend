package com.nixora.loan.GoogleCalendarIntegration.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.GoogleCalendarIntegration.entity.CalendarEventDTO;
import com.nixora.loan.GoogleCalendarIntegration.services.GoogleCalendarService;
import com.nixora.loan.LoanSchedule.entity.LoanScheduleEvent;
import com.nixora.loan.LoanSchedule.repository.LoanScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan/calendar")
@RequiredArgsConstructor
public class LoanCalendarController {

    private final LoanScheduleRepository repo;
    private final GoogleCalendarService calendar;

    @PostMapping("/{loanId}/sync")
    public ResponseEntity<?> syncLoan(
            @PathVariable UUID loanId,
            @AuthenticationPrincipal User user
    ) throws Exception {

        List<LoanScheduleEvent> events =
                repo.findByLoanIdOrderByEventDate(loanId);

        if (events.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("No schedule found for loan " + loanId);
        }

        calendar.pushEvents(user, loanId, events);

        List<CalendarEventDTO> responseEvents =
                events.stream()
                        .map(e -> new CalendarEventDTO(
                                e.getType().name().replace("_", " "),
                                e.getDescription(),
                                e.getEventDate()
                        ))
                        .toList();

        return ResponseEntity.ok(
                Map.of(
                        "eventsSynced", responseEvents.size(),
                        "status", "Calendar synced",
                        "events", responseEvents
                )
        );

    }
}
