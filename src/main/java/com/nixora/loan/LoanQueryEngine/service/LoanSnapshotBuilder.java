package com.nixora.loan.LoanQueryEngine.service;

import com.nixora.loan.LoanQueryEngine.entities.LoanSnapshotEntity;
import com.nixora.loan.document.dto.LmaLoanData;
import com.nixora.loan.document.entities.LoanDocument;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.nixora.loan.document.dto.*;


@Component
public class LoanSnapshotBuilder {

    public LoanSnapshotEntity from(LoanDocument doc) {

        LoanSnapshotEntity s = new LoanSnapshotEntity();
        s.setLoanId(doc.getLoanId());
        s.setUploadedBy(doc.getUploadedBy());

        LmaLoanData d = doc.getLoanData();
        if (d == null) return s;

        /* ================================
           PARTIES
        ================================= */
        Parties p = d.getParties();
        if (p != null) {
            s.setBorrower(val(p.getBorrower()));
            s.setParent(val(p.getParent()));
            s.setGuarantor(val(p.getGuarantor()));

            s.setObligors(valList(p.getObligors()));
            s.setLenders(valList(p.getLenders()));
            s.setOriginalLenders(valList(p.getOriginalLenders()));
            s.setFinanceParties(valList(p.getFinanceParties()));

            s.setFacilityAgent(val(p.getFacilityAgent()));
            s.setSecurityAgent(val(p.getSecurityAgent()));
            s.setArranger(val(p.getArranger()));
            s.setBookRunner(val(p.getBookrunner()));
        }


        /* ================================
           FACILITY (PRIMARY)
        ================================= */
        List<Facility> facilities = d.getFacilities();
        if (facilities != null && !facilities.isEmpty()) {

            Facility f = facilities.get(0); // lead / primary facility

            s.setFacilityId(val(f.getFacilityId()));
            s.setFacilityType(val(f.getFacilityType()));
            s.setCurrency(val(f.getCurrency()));
            s.setMaturityDate(parseDate(f.getFinalMaturityDate()));

            /* ---------- Interest ---------- */
            InterestTerms interest = f.getInterest();
            if (interest != null) {
                s.setBenchmark(val(interest.getBenchmark()));
                s.setMargin(parsePercent(interest.getMargin()));
                s.setInterestPeriod(val(interest.getInterestPeriod()));
            }
        }

        /* ================================
           TRANSFERABILITY
        ================================= */
        TransferProvisions t = d.getTransfers();
        if (t != null) {
            s.setWhiteList(val(t.getWhiteList()));
        }

        /* ================================
           RISK
        ================================= */
        Covenants c = d.getCovenants();
        int covCount = 0;
        if (c != null) {
            covCount += size(c.getFinancial());
            covCount += size(c.getInformation());
            covCount += size(c.getGeneral());
        }
        s.setCovenantCount(covCount);

        EventsOfDefault eod = d.getEventsOfDefault();
        if (eod != null) {
            int defaultCount = 0;

            defaultCount += size(eod.getNonPayment());
            defaultCount += size(eod.getFinancialCovenantBreach());
            defaultCount += size(eod.getOtherObligationBreach());
            defaultCount += size(eod.getMisrepresentation());
            defaultCount += size(eod.getCrossDefault());
            defaultCount += size(eod.getInsolvency());
            defaultCount += size(eod.getCreditorsProcess());
            defaultCount += size(eod.getUnlawfulness());
            defaultCount += size(eod.getChangeOfControl());
            defaultCount += size(eod.getRepudiation());
            defaultCount += size(eod.getAuditQualification());

            s.setDefaultCount(defaultCount);
        }


        /* ================================
           GOVERNING LAW
        ================================= */
        GoverningLaw g = d.getGoverningLaw();
        if (g != null) {
            s.setGoverningLaw(val(g.getGoverningLaw()));
            s.setJurisdiction(val(g.getJurisdiction()));
        }

        return s;
    }

    /* ================================
       Helpers
    ================================= */

    /* ================================
       Helpers (Corrected for Flat Strings)
    ================================= */

    // Changed: Accepts String directly instead of ExtractedField
    private String val(String f) {
        return f;
    }

    // Changed: Accepts List<String> instead of List<ExtractedField>
    private String valList(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining("; "));
    }

    // Changed: Accepts String directly
    private Double parsePercent(String f) {
        try {
            if (f == null) return null;
            return Double.parseDouble(f.replace("%", "").trim());
        } catch (Exception e) {
            return null;
        }
    }

    // Changed: Accepts String directly
    private LocalDate parseDate(String f) {
        try {
            if (f == null) return null;
            return LocalDate.parse(f.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private int size(List<?> list) {
        return list == null ? 0 : list.size();
    }
}
