package com.nixora.loan.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LmaLoanData {

    private Parties parties;                 // Clause 1–2
    private List<Facility> facilities;       // Clause 2–5

    private Dates dates;

    private InterestTerms interest;
    private FeeTerms fees;
    private PrepaymentTerms prepayment;
    private RepaymentTerms repayment;
    private UtilisationTerms utilisation;

    private TransferProvisions transferProvisions;

    private Covenants covenants;             // Clause 15
    private EventsOfDefault eventsOfDefault; // Clause 16
    private TransferProvisions transfers;    // Clause 24
    private GoverningLaw governingLaw;       // Clause 40
    private Representations representations; // Clause 14



    @JsonProperty("facility")
    public void setFacility(Facility facility) {
        this.facilities = List.of(facility);
    }

}



