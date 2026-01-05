package com.nixora.loan.LoanQueryEngine.entities;

import com.nixora.auth.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "loan_snapshots")
@Getter
@Setter
public class LoanSnapshotEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID loanId;

    @Column(nullable = true)
    private String facilityId;


    @ManyToOne
    private User uploadedBy;

    /* --------------------
       FACILITY
    -------------------- */
    @Column(columnDefinition = "TEXT")
    private String facilityType;
    @Column(columnDefinition = "TEXT")
    private String currency;
    private LocalDate maturityDate;

    /* --------------------
       INTEREST
    -------------------- */
    private Double margin;
    @Column(columnDefinition = "TEXT")
    private String benchmark;
    @Column(columnDefinition = "TEXT")
    private String interestPeriod;

    /* --------------------
       PARTIES
    -------------------- */
    @Column(columnDefinition = "TEXT")
    private String borrower;
    @Column(columnDefinition = "TEXT")
    private String parent;
    @Column(columnDefinition = "TEXT")
    private String guarantor;
    @Column(columnDefinition = "TEXT")
    private String obligors;
    @Column(columnDefinition = "TEXT")
    private String lenders;
    @Column(columnDefinition = "TEXT")
    private String originalLenders;
    @Column(columnDefinition = "TEXT")
    private String financeParties;
    @Column(columnDefinition = "TEXT")
    private String securityAgent;
    @Column(columnDefinition = "TEXT")
    private String arranger;
    @Column(columnDefinition = "TEXT")
    private String bookRunner;
    @Column(columnDefinition = "TEXT")
    private String facilityAgent;

    /* --------------------
       TRANSFERABILITY
    -------------------- */
    @Column(columnDefinition = "TEXT")
    private String whiteList;



    private LocalDate agreementDate;
    private Boolean prepaymentAllowed;
    @Column(columnDefinition = "TEXT")
    private String transferRestrictions;


    /* --------------------
       RISK
    -------------------- */
    private Integer covenantCount;
    private Integer defaultCount;

    /* --------------------
       LEGAL
    -------------------- */
    @Column(columnDefinition = "TEXT")
    private String governingLaw;
    @Column(columnDefinition = "TEXT")
    private String jurisdiction;
}
