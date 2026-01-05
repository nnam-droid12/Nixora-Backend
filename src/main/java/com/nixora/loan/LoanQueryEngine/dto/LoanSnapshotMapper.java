package com.nixora.loan.LoanQueryEngine.dto;


import com.nixora.loan.LoanQueryEngine.entities.LoanSnapshotEntity;

public class LoanSnapshotMapper {

    public static LoanSnapshotDTO toDto(LoanSnapshotEntity e) {
        return new LoanSnapshotDTO(
                e.getLoanId(),
                e.getBorrower(),
                e.getFacilityAgent(),
                e.getFacilityType(),
                e.getCurrency(),
                e.getMargin(),
                e.getBenchmark(),
                e.getInterestPeriod(),
                e.getAgreementDate(),
                e.getMaturityDate(),
                e.getPrepaymentAllowed(),
                e.getTransferRestrictions(),
                e.getCovenantCount(),
                e.getDefaultCount(),
                e.getGoverningLaw(),
                e.getJurisdiction()
        );
    }
}

