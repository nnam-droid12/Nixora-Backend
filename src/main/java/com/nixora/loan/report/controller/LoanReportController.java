package com.nixora.loan.report.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.report.dto.HighRiskLoanRow;
import com.nixora.loan.report.dto.MaturityExposureRow;
import com.nixora.loan.report.dto.PortfolioSummaryDTO;
import com.nixora.loan.report.service.LoanReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class LoanReportController {

    private final LoanReportService service;

    @GetMapping("/portfolio")
    public PortfolioSummaryDTO portfolio(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.portfolioSummary(user);
    }

    @GetMapping("/maturity")
    public List<MaturityExposureRow> maturity(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.maturityExposure(user);
    }

    @GetMapping("/high-risk")
    public List<HighRiskLoanRow> highRisk(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.highRiskLoans(user);
    }
}
