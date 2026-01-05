package com.nixora.loan.LoanComparison.service;

import com.nixora.auth.entities.User;
import com.nixora.loan.LoanComparison.dto.LoanComparisonResult;
import com.nixora.loan.LoanComparison.dto.LoanSnapshot;
import com.nixora.loan.LoanComparison.repositories.LoanComparisonRepository;
import com.nixora.loan.document.dto.*;
import com.nixora.loan.document.entities.LoanDocument;
import com.nixora.loan.document.exception.TwoOrMoreLoansException;
import com.nixora.loan.document.repositories.LoanDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static java.nio.file.Files.size;

@Service
@RequiredArgsConstructor
public class LoanComparisonService {

    private final LoanComparisonRepository repository;

    public LoanComparisonResult compare(List<UUID> loanIds, User user) {

        List<LoanDocument> documents =
                repository.findByLoanIdInAndUploadedBy(loanIds, user);

        if (documents.size() < 2) {
            throw new TwoOrMoreLoansException("You must own at least two of the selected loans");
        }

        List<LoanSnapshot> snapshots = documents.stream()
                .map(this::toSnapshot)
                .toList();

        Map<String, UUID> insights = new HashMap<>();

        insights.put("bestMargin", highestMargin(snapshots));
        insights.put("longestMaturity", latestMaturity(snapshots));
        insights.put("mostTransferable", mostTransferable(snapshots));
        insights.put("mostLenderFriendly", strongestCovenants(snapshots));

        LoanComparisonResult result = new LoanComparisonResult();
        result.setLoans(snapshots);
        result.setComparison(insights);

        return result;
    }


    private LoanSnapshot toSnapshot(LoanDocument doc) {

        LmaLoanData d = doc.getLoanData();

        LoanSnapshot s = new LoanSnapshot();
        s.setLoanId(doc.getLoanId());

        if (d == null) return s;

        if (d.getFacilities() != null && !d.getFacilities().isEmpty()) {
            Facility f = d.getFacilities().get(0);

            if (f.getInterest() != null) {
                // CHANGED: Access f.getInterest().getMargin() directly (it's now a String)
                s.setMargin(f.getInterest().getMargin());
                s.setBenchmark(f.getInterest().getBenchmark());
            }

            if (f.getFinalMaturityDate() != null) {
                // CHANGED: Access directly
                String m = f.getFinalMaturityDate();
                s.setMaturity(m);

                Integer tenor = parseTenorMonths(m);
                if (tenor != null) {
                    s.setTenorMonths(tenor);
                }
            }
        }

        if (d.getTransfers() != null) {
            // CHANGED: Access directly
            s.setTransferRestrictions(d.getTransfers().getWhiteList());
        }

        Covenants c = d.getCovenants();
        if (c != null) {
            int covCount = 0;
            covCount += size(c.getFinancial());
            covCount += size(c.getInformation());
            covCount += size(c.getGeneral());
            s.setCovenantCount(covCount);
        } else {
            s.setCovenantCount(0);
        }

        EventsOfDefault e = d.getEventsOfDefault();
        if (e != null) {
            int defaultCount = 0;
            defaultCount += size(e.getNonPayment());
            defaultCount += size(e.getFinancialCovenantBreach());
            defaultCount += size(e.getOtherObligationBreach());
            defaultCount += size(e.getMisrepresentation());
            defaultCount += size(e.getCrossDefault());
            defaultCount += size(e.getInsolvency());
            defaultCount += size(e.getCreditorsProcess());
            defaultCount += size(e.getUnlawfulness());
            defaultCount += size(e.getChangeOfControl());
            defaultCount += size(e.getRepudiation());
            defaultCount += size(e.getAuditQualification());

            s.setDefaultCount(defaultCount);
        } else {
            s.setDefaultCount(0);
        }

        if (d.getGoverningLaw() != null) {
            // CHANGED: Access directly
            s.setGoverningLaw(d.getGoverningLaw().getGoverningLaw());
        }

        return s;
    }

    private int size(List<?> list) {
        return list == null ? 0 : list.size();
    }





    private Integer parseTenorMonths(String raw) {
        if (raw == null) return null;

        raw = raw.toLowerCase();

        var yearMatcher = java.util.regex.Pattern.compile("(\\d+)\\s*year").matcher(raw);
        if (yearMatcher.find()) {
            return Integer.parseInt(yearMatcher.group(1)) * 12;
        }

        var monthMatcher = java.util.regex.Pattern.compile("(\\d+)\\s*month").matcher(raw);
        if (monthMatcher.find()) {
            return Integer.parseInt(monthMatcher.group(1));
        }

        return null;
    }


    private UUID highestMargin(List<LoanSnapshot> loans) {
        return loans.stream()
                .max(Comparator.comparingDouble(l -> parsePercentSafe(l.getMargin())))
                .map(LoanSnapshot::getLoanId)
                .orElse(null);
    }


    private double parsePercentSafe(String s) {
        if (s == null) return 0;
        try {
            return Double.parseDouble(s.replace("%", ""));
        } catch (Exception e) {
            return 0;
        }
    }



    private UUID latestMaturity(List<LoanSnapshot> loans) {
        return loans.stream()
                .filter(l -> resolveMaturity(l) != null)
                .max(Comparator.comparing(
                        l -> resolveMaturity(l),
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .map(LoanSnapshot::getLoanId)
                .orElse(null);
    }





    private LocalDate resolveMaturity(LoanSnapshot l) {

        if (l.getMaturity() != null) {
            try {
                return LocalDate.parse(l.getMaturity());
            } catch (Exception ignored) {}
        }

        if (l.getTenorMonths() != null) {

            if (l.getUtilisationDate() != null) {
                try {
                    return LocalDate.parse(l.getUtilisationDate())
                            .plusMonths(l.getTenorMonths());
                } catch (Exception ignored) {}
            }

            return LocalDate.now().plusMonths(l.getTenorMonths());
        }

        return null;
    }




    private UUID mostTransferable(List<LoanSnapshot> loans) {
        return loans.stream()
                .max(Comparator.comparingInt(l -> scoreTransferability(l.getTransferRestrictions())))
                .map(LoanSnapshot::getLoanId)
                .orElse(null);
    }

    private int scoreTransferability(String s) {
        if (s == null) return 0;
        s = s.toLowerCase();
        if (s.contains("free")) return 3;
        if (s.contains("consent")) return 1;
        if (s.contains("prohibit")) return 0;
        return 0;
    }


    private UUID strongestCovenants(List<LoanSnapshot> loans) {
        return loans.stream()
                .max(Comparator.comparingInt(this::covenantScore))
                .map(LoanSnapshot::getLoanId)
                .orElse(null);
    }

    private int covenantScore(LoanSnapshot l) {
        int score = 0;
        score += l.getCovenantCount();
        score += l.getDefaultCount();
        return score;
    }

}
