package com.nixora.loan.document.extractor;


import com.nixora.loan.document.dto.*;
import com.nixora.loan.document.entities.LoanDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LoanApiMapper {

    public LoanApiResponse toResponse(LoanDocument doc) {
        if (doc == null) return null;

        LmaLoanData d = doc.getLoanData();
        LoanApiResponse r = new LoanApiResponse();
        r.setLoanId(doc.getLoanId());

        if (d == null) return r;


        r.setParties(mapParties(d.getParties()));
        r.setFacility(mapFacility(d.getFacilities()));

        r.setInterest(mapInterest(d.getInterest()));
        r.setFees(mapFees(d.getFees()));
        r.setRepayment(mapRepayment(d.getRepayment()));
        r.setPrepayment(mapPrepayment(d.getPrepayment()));
        r.setUtilisation(mapUtilisation(d.getUtilisation()));
        r.setRepresentations(mapReps(d.getRepresentations()));

        r.setCovenants(mapCovenants(d.getCovenants()));
        r.setEventsOfDefault(mapEvents(d.getEventsOfDefault()));
        r.setTransfers(mapTransfers(d.getTransfers()));
        r.setGoverningLaw(mapLaw(d.getGoverningLaw()));

        return r;
    }

    /* ============================= PARTIES ============================= */

    private PartiesDto mapParties(Parties p) {
        if (p == null) return null;

        PartiesDto dto = new PartiesDto();
        dto.setBorrower(val(p.getBorrower()));
        dto.setParent(val(p.getParent()));
        dto.setGuarantor(val(p.getGuarantor()));

        dto.setObligors(vals(p.getObligors()));
        dto.setLenders(vals(p.getLenders()));
        dto.setOriginalLenders(vals(p.getOriginalLenders()));
        dto.setFinanceParties(vals(p.getFinanceParties()));

        dto.setFacilityAgent(val(p.getFacilityAgent()));
        dto.setSecurityAgent(val(p.getSecurityAgent()));
        dto.setArranger(val(p.getArranger()));
        dto.setBookrunner(val(p.getBookrunner()));

        return dto;
    }

    /* ============================= FACILITY ============================= */

    private FacilityDTO mapFacility(List<Facility> facilities) {
        if (facilities == null || facilities.isEmpty() || facilities.get(0) == null) return null;

        Facility f = facilities.get(0);
        FacilityDTO dto = new FacilityDTO();
        dto.setFacilityId(val(f.getFacilityId()));
        dto.setFacilityType(val(f.getFacilityType()));
        dto.setFacilityName(val(f.getFacilityName()));
        dto.setCurrency(val(f.getCurrency()));
        dto.setFacilityAmount(val(f.getFacilityAmount()));
        dto.setCommitment(val(f.getCommitment()));
        dto.setAvailabilityPeriod(val(f.getAvailabilityPeriod()));
        dto.setDrawstop(val(f.getDrawstop()));
        dto.setFinalMaturityDate(val(f.getFinalMaturityDate()));
        dto.setRepaymentProfile(val(f.getRepaymentProfile()));
        dto.setPurpose(val(f.getPurpose()));

        return dto;
    }

    private InterestTermsDTO mapInterest(InterestTerms i) {
        if (i == null) return null;
        InterestTermsDTO dto = new InterestTermsDTO();
        dto.setBenchmark(val(i.getBenchmark()));
        dto.setMargin(val(i.getMargin()));
        dto.setCreditAdjustmentSpread(val(i.getCreditAdjustmentSpread()));
        dto.setInterestPeriod(val(i.getInterestPeriod()));
        dto.setInterestPaymentDate(val(i.getInterestPaymentDate()));
        dto.setBreakCosts(val(i.getBreakCosts()));
        dto.setDefaultInterest(val(i.getDefaultInterest()));
        dto.setDayCountFraction(val(i.getDayCountFraction()));
        dto.setCompoundingMethod(val(i.getCompoundingMethod()));
        dto.setRollover(val(i.getRollover()));
        return dto;
    }

    private FeeTermsDTO mapFees(FeeTerms f) {
        if (f == null) return null;
        FeeTermsDTO dto = new FeeTermsDTO();
        dto.setArrangementFee(val(f.getArrangementFee()));
        dto.setParticipationFee(val(f.getParticipationFee()));
        dto.setCommitmentFee(val(f.getCommitmentFee()));
        dto.setUtilisationFee(val(f.getUtilisationFee()));
        dto.setAgencyFee(val(f.getAgencyFee()));
        dto.setCancellationFee(val(f.getCancellationFee()));
        dto.setFrontEndFee(val(f.getFrontEndFee()));
        return dto;
    }

    private RepaymentTermsDTO mapRepayment(RepaymentTerms r) {
        if (r == null) return null;
        RepaymentTermsDTO dto = new RepaymentTermsDTO();
        dto.setRepaymentDate(val(r.getRepaymentDate()));
        dto.setRepaymentInstalments(val(r.getRepaymentInstalments()));
        dto.setAmortisationSchedule(val(r.getAmortisationSchedule()));
        dto.setBalloonPayment(val(r.getBalloonPayment()));
        dto.setVoluntaryPrepayment(bool(r.getVoluntaryPrepayment()));
        dto.setMandatoryPrepayment(bool(r.getMandatoryPrepayment()));
        dto.setChangeOfControlPrepayment(bool(r.getChangeOfControlPrepayment()));
        dto.setIllegalityPrepayment(bool(r.getIllegalityPrepayment()));
        dto.setTaxGrossUpPrepayment(bool(r.getTaxGrossUpPrepayment()));
        return dto;
    }

    private PrepaymentTermsDTO mapPrepayment(PrepaymentTerms p) {
        if (p == null) return null;
        PrepaymentTermsDTO dto = new PrepaymentTermsDTO();
        dto.setPrepaymentType(val(p.getPrepaymentType()));
        dto.setVoluntary(bool(p.getVoluntary()));
        dto.setAssetSale(bool(p.getAssetSale()));
        dto.setInsuranceProceeds(bool(p.getInsuranceProceeds()));
        dto.setDebtIssuance(bool(p.getDebtIssuance()));
        dto.setChangeOfControl(bool(p.getChangeOfControl()));
        dto.setIllegality(bool(p.getIllegality()));
        dto.setTax(bool(p.getTax()));
        dto.setBreakCosts(val(p.getBreakCosts()));
        dto.setPrepaymentFee(val(p.getPrepaymentFee()));
        return dto;
    }

    private UtilisationTermsDTO mapUtilisation(UtilisationTerms u) {
        if (u == null) return null;
        UtilisationTermsDTO dto = new UtilisationTermsDTO();
        dto.setUtilisationRequest(val(u.getUtilisationRequest()));
        dto.setUtilisationDate(val(u.getUtilisationDate()));
        dto.setUtilisationAmount(val(u.getUtilisationAmount()));
        dto.setUtilisationCurrency(val(u.getUtilisationCurrency()));
        dto.setUtilisationNotice(val(u.getUtilisationNotice()));
        dto.setMinimumAmount(val(u.getMinimumAmount()));
        dto.setMultipleDrawings(bool(u.getMultipleDrawings()));
        dto.setCancelledCommitments(val(u.getCancelledCommitments()));
        return dto;
    }

    /* ============================= COVENANTS ============================= */

    private CovenantsDTO mapCovenants(Covenants c) {
        if (c == null) return null;

        CovenantsDTO dto = new CovenantsDTO();
        dto.setFinancial(vals(c.getFinancial()));

        dto.setInformation(vals(c.getInformation()));
        dto.setGeneral(vals(c.getGeneral()));
        dto.setNegativePledge(vals(c.getNegativePledge()));
        dto.setDisposals(vals(c.getDisposals()));
        dto.setMergers(vals(c.getMergers()));
        dto.setChangeOfBusiness(vals(c.getChangeOfBusiness()));
        dto.setIndebtedness(vals(c.getIndebtedness()));
        dto.setGuarantees(vals(c.getGuarantees()));

        return dto;
    }

    /* ============================= OTHER SECTIONS ============================= */

    private RepresentationsDTO mapReps(Representations r) {
        if (r == null) return null;
        RepresentationsDTO dto = new RepresentationsDTO();
        dto.setBindingObligations(vals(r.getBindingObligations()));
        dto.setNonConflict(vals(r.getNonConflict()));
        dto.setPowerAndAuthority(vals(r.getPowerAndAuthority()));
        dto.setNoDefault(vals(r.getNoDefault()));
        dto.setFinancialStatements(vals(r.getFinancialStatements()));
        dto.setLitigation(vals(r.getLitigation()));
        dto.setEnvironmental(vals(r.getEnvironmental()));
        dto.setTax(vals(r.getTax()));
        return dto;
    }

    private EventsOfDefaultDTO mapEvents(EventsOfDefault e) {
        if (e == null) return null;
        EventsOfDefaultDTO dto = new EventsOfDefaultDTO();
        dto.setNonPayment(vals(e.getNonPayment()));
        dto.setFinancialCovenantBreach(vals(e.getFinancialCovenantBreach()));
        dto.setOtherObligationBreach(vals(e.getOtherObligationBreach()));
        dto.setMisrepresentation(vals(e.getMisrepresentation()));
        dto.setCrossDefault(vals(e.getCrossDefault()));
        dto.setInsolvency(vals(e.getInsolvency()));
        dto.setCreditorsProcess(vals(e.getCreditorsProcess()));
        dto.setUnlawfulness(vals(e.getUnlawfulness()));
        dto.setChangeOfControl(vals(e.getChangeOfControl()));
        dto.setRepudiation(vals(e.getRepudiation()));
        dto.setAuditQualification(vals(e.getAuditQualification()));
        return dto;
    }

    private TransferProvisionsDTO mapTransfers(TransferProvisions t) {
        if (t == null) return null;
        TransferProvisionsDTO dto = new TransferProvisionsDTO();
        dto.setQualifyingLender(bool(t.getQualifyingLender()));
        dto.setTransferCertificate(val(t.getTransferCertificate()));
        dto.setAssignmentAgreement(val(t.getAssignmentAgreement()));
        dto.setMinimumHolding(val(t.getMinimumHolding()));
        dto.setWhiteList(val(t.getWhiteList()));
        dto.setBlackList(val(t.getBlackList()));
        dto.setDefaultingLender(bool(t.getDefaultingLender()));
        dto.setYankTheBank(bool(t.getYankTheBank()));
        return dto;
    }

    private GoverningLawDTO mapLaw(GoverningLaw g) {
        if (g == null) return null;
        GoverningLawDTO dto = new GoverningLawDTO();
        dto.setGoverningLaw(val(g.getGoverningLaw()));
        dto.setJurisdiction(val(g.getJurisdiction()));
        return dto;
    }

    /* ============================= HELPERS ============================= */

    private String val(String f) {
        return (f == null || f.trim().isEmpty()) ? "N/A" : f;
    }

    private Boolean bool(Boolean f) {
        return f != null && f;
    }

    private List<String> vals(List<String> list) {
        if (list == null) return List.of();
        return list.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
}