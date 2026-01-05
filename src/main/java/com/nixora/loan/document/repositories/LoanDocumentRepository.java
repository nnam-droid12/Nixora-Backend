package com.nixora.loan.document.repositories;

import com.nixora.auth.entities.User;
import com.nixora.loan.document.entities.LoanDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanDocumentRepository extends JpaRepository<LoanDocument, UUID> {


    Optional<LoanDocument> findByLoanId(UUID loanId);

    List<LoanDocument> findAllByUploadedBy(User user);

    boolean existsByOriginalFileNameAndUploadedBy(String name, User user);

    boolean existsByLoanIdAndUploadedBy(UUID loanId, User user);

    void deleteByLoanIdAndUploadedBy(UUID loanId, User user);




    @Query("SELECT DISTINCT d FROM LoanDocument d " +
            "LEFT JOIN LoanCollaborator c ON d.id = c.loan.id " +
            "WHERE d.uploadedBy = :user OR c.user = :user")
    List<LoanDocument> findAccessibleLoans(@Param("user") User user);

    Optional<LoanDocument> findByLoanIdAndUploadedBy(UUID loanId, User owner);



    @Query("SELECT DISTINCT d FROM LoanDocument d " +
            "LEFT JOIN LoanCollaborator c ON d.id = c.loan.id " +
            "WHERE d.uploadedBy = :user OR c.user = :user")
    List<LoanDocument> findAllAccessible(@Param("user") User user);

    // 2. FIX for getLoanById: Finds a specific document if you are OWN or COLLABORATOR
    @Query("SELECT d FROM LoanDocument d " +
            "LEFT JOIN LoanCollaborator c ON d.id = c.loan.id " +
            "WHERE d.loanId = :loanId AND (d.uploadedBy = :user OR c.user = :user)")
    Optional<LoanDocument> findAccessibleLoan(@Param("loanId") UUID loanId, @Param("user") User user);





}


