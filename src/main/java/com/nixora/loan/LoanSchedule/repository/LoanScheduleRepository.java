package com.nixora.loan.LoanSchedule.repository;

import com.nixora.auth.entities.User;
import com.nixora.loan.LoanSchedule.entity.LoanScheduleEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LoanScheduleRepository
        extends JpaRepository<LoanScheduleEvent, UUID> {

    List<LoanScheduleEvent> findByLoanIdOrderByEventDate(UUID loanId);

    @Query("""
        SELECT e FROM LoanScheduleEvent e
        JOIN LoanDocument d ON e.loanId = d.loanId
        WHERE d.uploadedBy = :user
    """)
    List<LoanScheduleEvent> findAllByUser(User user);

    void deleteByLoanId(UUID loanId);

    List<LoanScheduleEvent> findByEventDate(LocalDate eventDate);

}
