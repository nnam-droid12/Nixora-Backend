package com.nixora.loan.document.repositories;

import com.nixora.auth.entities.User;
import com.nixora.loan.document.entities.LoanDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanDocumentRepository extends JpaRepository<LoanDocument, UUID> {

    Optional<LoanDocument> findByLoanIdAndUploadedBy(UUID loanId, User user);

    Optional<LoanDocument> findByLoanId(UUID loanId);

    List<LoanDocument> findAllByUploadedBy(User user);

    boolean existsByOriginalFileNameAndUploadedBy(String name, User user);

    boolean existsByLoanIdAndUploadedBy(UUID loanId, User user);

    void deleteByLoanIdAndUploadedBy(UUID loanId, User user);


    @Query("""
SELECT l FROM LoanDocument l
LEFT JOIN LoanCollaborator c ON c.loan = l AND c.user = :user
WHERE l.loanId = :loanId
AND (l.uploadedBy = :user OR c.user IS NOT NULL)
""")
    Optional<LoanDocument> findAccessibleLoan(UUID loanId, User user);


}


