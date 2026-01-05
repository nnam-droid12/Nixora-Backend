package com.nixora.loan.document.repositories;

import com.nixora.loan.document.entities.ExtractedFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExtractedFieldRepository
        extends JpaRepository<ExtractedFieldEntity, UUID> {

    Optional<ExtractedFieldEntity> findByLoanIdAndFieldKey(
            UUID loanId,
            String fieldKey
    );
}
