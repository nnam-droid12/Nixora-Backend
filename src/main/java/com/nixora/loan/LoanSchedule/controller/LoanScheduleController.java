package com.nixora.loan.LoanSchedule.controller;

import com.nixora.loan.LoanSchedule.dto.LoanScheduleEventDTO;
import com.nixora.loan.LoanSchedule.repository.LoanScheduleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
public class LoanScheduleController {

    private final LoanScheduleRepository repo;

    public LoanScheduleController(LoanScheduleRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{loanId}/schedule")
    public List<LoanScheduleEventDTO> timeline(
            @PathVariable UUID loanId,
            Authentication auth) {

        return repo.findByLoanIdOrderByEventDate(loanId)
                .stream()
                .map(e -> new LoanScheduleEventDTO(
                        e.getEventDate(),
                        e.getType().name(),
                        e.getDescription()
                ))
                .toList();
    }
}
