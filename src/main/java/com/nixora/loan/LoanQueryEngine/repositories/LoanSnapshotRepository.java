package com.nixora.loan.LoanQueryEngine.repositories;

import com.nixora.auth.entities.User;
import com.nixora.loan.LoanQueryEngine.entities.LoanSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoanSnapshotRepository
        extends JpaRepository<LoanSnapshotEntity, UUID> {

    List<LoanSnapshotEntity> findByUploadedBy(User user);

    void deleteByLoanId(UUID loanId);
}
