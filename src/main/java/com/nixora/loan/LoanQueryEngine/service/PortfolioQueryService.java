package com.nixora.loan.LoanQueryEngine.service;

import com.nixora.auth.entities.User;
import com.nixora.loan.LoanQueryEngine.dto.LoanQueryRequest;
import com.nixora.loan.LoanQueryEngine.entities.LoanSnapshotEntity;
import com.nixora.loan.LoanQueryEngine.repositories.LoanSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioQueryService {

    private final LoanSnapshotRepository repo;

    public List<LoanSnapshotEntity> query(LoanQueryRequest q, User user) {

        List<LoanSnapshotEntity> loans = repo.findByUploadedBy(user);

        var f = q.getFilters();

        return loans.stream()

                .filter(l -> f == null || f.getBenchmark() == null ||
                        eq(l.getBenchmark(), f.getBenchmark()))

                .filter(l -> f == null || f.getFacilityType() == null ||
                        eq(l.getFacilityType(), f.getFacilityType()))

                .filter(l -> f == null || f.getCurrency() == null ||
                        eq(l.getCurrency(), f.getCurrency()))

                .filter(l -> f == null || f.getBorrower() == null ||
                        contains(l.getBorrower(), f.getBorrower()))

                .filter(l -> f == null || f.getFacilityAgent() == null ||
                        contains(l.getFacilityAgent(), f.getFacilityAgent()))

                .filter(l -> f == null || f.getGoverningLaw() == null ||
                        contains(l.getGoverningLaw(), f.getGoverningLaw()))

                .filter(l -> f == null || f.getJurisdiction() == null ||
                        contains(l.getJurisdiction(), f.getJurisdiction()))

                .filter(l -> f == null || f.getMinMargin() == null ||
                        safe(l.getMargin()) >= f.getMinMargin())

                .filter(l -> f == null || f.getMaxMargin() == null ||
                        safe(l.getMargin()) <= f.getMaxMargin())

                .filter(l -> f == null || f.getMaxMonthsToMaturity() == null ||
                        (l.getMaturityDate() != null &&
                                l.getMaturityDate().isBefore(
                                        LocalDate.now().plusMonths(f.getMaxMonthsToMaturity())
                                )))

                .filter(l -> f == null || f.getAgreementAfter() == null ||
                        (l.getAgreementDate() != null &&
                                l.getAgreementDate().isAfter(f.getAgreementAfter())))

                .filter(l -> f == null || f.getTransferableOnly() == null ||
                        !f.getTransferableOnly() || isTransferable(l))

                .filter(l -> f == null || f.getPrepaymentAllowed() == null ||
                        f.getPrepaymentAllowed().equals(l.getPrepaymentAllowed()))

                .filter(l -> keywordListMatch(l, q.getKeywords()))

                .toList();
    }

    private boolean keywordListMatch(LoanSnapshotEntity l, List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) return true;

        for (String k : keywords) {
            if (keywordMatch(l, k)) {
                return true;   // OR semantics
            }
        }
        return false;
    }


    private boolean isTransferable(LoanSnapshotEntity l) {
        if (l.getTransferRestrictions() == null) return true;
        String s = l.getTransferRestrictions().toLowerCase();
        return !s.contains("prohibit") && !s.contains("consent");
    }

    private boolean keywordMatch(LoanSnapshotEntity l, String k) {
        if (k == null) return true;
        k = k.toLowerCase();

        return contains(l.getBenchmark(), k)
                || contains(l.getFacilityType(), k)
                || contains(l.getBorrower(), k)
                || contains(l.getFacilityAgent(), k)
                || contains(l.getGoverningLaw(), k)
                || contains(l.getJurisdiction(), k);
    }

    private boolean contains(String s, String k) {
        return s != null && s.toLowerCase().contains(k.toLowerCase());
    }

    private boolean eq(String a, String b) {
        return a != null && a.equalsIgnoreCase(b);
    }

    private double safe(Double d) {
        return d == null ? 0 : d;
    }
}

