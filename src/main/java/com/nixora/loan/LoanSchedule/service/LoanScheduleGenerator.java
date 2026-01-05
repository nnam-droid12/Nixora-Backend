package com.nixora.loan.LoanSchedule.service;

import com.nixora.loan.LoanSchedule.entity.LoanScheduleEvent;
import com.nixora.loan.LoanSchedule.entity.ScheduleEventType;
import com.nixora.loan.LoanSchedule.util.LmaDateParser;
import com.nixora.loan.document.dto.*;
import com.nixora.loan.document.entities.LoanDocument;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanScheduleGenerator {

    public List<LoanScheduleEvent> generate(LoanDocument doc) {

        LmaLoanData data = doc.getLoanData();

        if (data == null) {
            return List.of();
        }

        if (data.getFacilities() == null || data.getFacilities().isEmpty()) {
            throw new IllegalStateException("No facility found in extracted loan data");
        }

        Facility facility = data.getFacilities().get(0);
        Dates dates = data.getDates();

        List<LoanScheduleEvent> events = new ArrayList<>();

        // ---------- START DATE ----------
        LocalDate start = null;

        // FIX: Removed .getValue() because effectiveDate is now a String
        if (dates != null && dates.getEffectiveDate() != null) {
            start = LmaDateParser.parse(dates.getEffectiveDate());
        }

        if (start == null && doc.getUploadedAt() != null) {
            start = doc.getUploadedAt().toLocalDate();
        }

        if (start == null) {
            throw new IllegalStateException("No start date could be resolved");
        }

        // ---------- MATURITY ----------
        LocalDate maturity = null;

        // FIX: Removed .getValue() because finalMaturityDate is now a String
        if (facility.getFinalMaturityDate() != null) {
            maturity = LmaDateParser.parse(facility.getFinalMaturityDate());
        }

        if (maturity == null) {
            maturity = start.plusYears(3);
        }

        // ---------- EVENTS ----------
        events.add(event(doc, start.plusMonths(3),
                ScheduleEventType.INTEREST_PAYMENT,
                "Interest payment due"));

        events.add(event(doc, start.plusYears(1),
                ScheduleEventType.COVENANT_TEST,
                "Annual covenant compliance test"));

        events.add(event(doc, maturity,
                ScheduleEventType.MATURITY,
                "Facility maturity"));

        return events;
    }

    private LoanScheduleEvent event(LoanDocument doc, LocalDate date,
                                    ScheduleEventType type, String desc) {

        LoanScheduleEvent e = new LoanScheduleEvent();
        // Null-safe check for IDs
        e.setLoanId(doc.getLoanId() != null ? doc.getLoanId() : doc.getId());
        e.setEventDate(date);
        e.setType(type);
        e.setDescription(desc);
        return e;
    }
}