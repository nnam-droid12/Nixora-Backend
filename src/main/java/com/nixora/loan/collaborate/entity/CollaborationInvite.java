package com.nixora.loan.collaborate.entity;

import com.nixora.auth.entities.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class CollaborationInvite {
    @Id
    @GeneratedValue
    private UUID id;
    private String token;

    @ManyToOne
    private User owner;

    private UUID loanId;

    private LocalDateTime expiresAt;
    private boolean used = false;
}
