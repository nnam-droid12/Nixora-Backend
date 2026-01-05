package com.nixora.loan.report.service;

import com.nixora.auth.entities.User;
import com.nixora.loan.report.dto.HighRiskLoanRow;
import com.nixora.loan.report.dto.MaturityExposureRow;
import com.nixora.loan.report.dto.PortfolioSummaryDTO;
import com.nixora.loan.report.repositories.LoanReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanReportService {

    private final LoanReportRepository repo;

    public PortfolioSummaryDTO portfolioSummary(User user) {
        PortfolioSummaryDTO dto = new PortfolioSummaryDTO();
        dto.setTotalLoans(repo.totalLoans(user));
        dto.setAverageMargin(
                Optional.ofNullable(repo.averageMargin(user)).orElse(0.0)
        );
        dto.setHighRiskLoans(repo.highRiskLoans(user));
        dto.setTransferableLoans(repo.transferableLoans(user));
        return dto;
    }

    public List<MaturityExposureRow> maturityExposure(User user) {
        return repo.maturityBuckets(user)
                .stream()
                .filter(r -> r[0] != null && r[1] != null)
                .map(r -> new MaturityExposureRow(
                        ((Number) r[0]).intValue(),
                        ((Number) r[1]).longValue()
                ))
                .toList();
    }


    public List<HighRiskLoanRow> highRiskLoans(User user) {
        return repo.highRisk(user).stream()
                .map(s -> new HighRiskLoanRow(
                        s.getLoanId(),
                        s.getCovenantCount(),
                        s.getDefaultCount()
                ))
                .toList();
    }
}
