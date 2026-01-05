package com.nixora.loan.collaborate.entity;

import com.nixora.auth.entities.User;
import com.nixora.loan.document.entities.LoanDocument;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"loan_id","user_id"})
)
@Data
public class LoanCollaborator {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private LoanDocument loan;

    @ManyToOne
    private User user;

    private boolean canEdit = true;

    private LocalDateTime grantedAt;
}
