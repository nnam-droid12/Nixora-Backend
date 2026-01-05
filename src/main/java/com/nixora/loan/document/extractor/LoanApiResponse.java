package com.nixora.loan.document.extractor;

import com.nixora.loan.document.dto.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class LoanApiResponse {

    private UUID loanId;

    private PartiesDto parties;
    private FacilityDTO facility;
    private InterestTermsDTO interest;
    private FeeTermsDTO fees;
    private RepaymentTermsDTO repayment;
    private PrepaymentTermsDTO prepayment;
    private UtilisationTermsDTO utilisation;

    private CovenantsDTO covenants;
    private EventsOfDefaultDTO eventsOfDefault;
    private RepresentationsDTO representations;
    private TransferProvisionsDTO transfers;
    private GoverningLawDTO governingLaw;
}
