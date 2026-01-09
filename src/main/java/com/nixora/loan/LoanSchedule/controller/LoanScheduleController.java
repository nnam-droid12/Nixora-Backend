package com.nixora.loan.LoanSchedule.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.LoanSchedule.dto.LoanScheduleEventDTO;
import com.nixora.loan.LoanSchedule.repository.LoanScheduleRepository;
import com.nixora.loan.LoanSchedule.service.LoanScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanScheduleController {

    private final LoanScheduleRepository repo;
    private final LoanScheduleService scheduleService;

    @GetMapping("/{loanId}/schedule")
    public List<LoanScheduleEventDTO> timeline(
            @PathVariable UUID loanId,
            @AuthenticationPrincipal User user) {


        scheduleService.notifyScheduleCreation(loanId, user);

        // 2. Return the data
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