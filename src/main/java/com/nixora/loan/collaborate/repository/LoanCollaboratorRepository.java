package com.nixora.loan.collaborate.repository;

import com.nixora.auth.entities.User;
import com.nixora.loan.collaborate.entity.LoanCollaborator;
import com.nixora.loan.document.entities.LoanDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanCollaboratorRepository
        extends JpaRepository<LoanCollaborator, UUID> {

    boolean existsByLoanAndUser(LoanDocument loan, User user);
}

