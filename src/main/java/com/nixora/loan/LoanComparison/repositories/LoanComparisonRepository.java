package com.nixora.loan.LoanComparison.repositories;

import com.nixora.auth.entities.User;
import com.nixora.loan.document.entities.LoanDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanComparisonRepository extends JpaRepository<LoanDocument, UUID> {

    List<LoanDocument> findByLoanIdInAndUploadedBy(List<UUID> loanIds, User user);


}



