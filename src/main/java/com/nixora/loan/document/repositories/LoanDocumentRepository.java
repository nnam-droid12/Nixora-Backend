package com.nixora.loan.document.repositories;

import com.nixora.loan.document.entities.LoanDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanDocumentRepository
        extends JpaRepository<LoanDocument, UUID> {
}
