package com.nixora.loan.TrelloIntegration.entity;

import com.nixora.auth.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class TrelloOAuthToken {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private User user;

    @Column(nullable = false, length = 200)
    private String token;

    private LocalDateTime connectedAt = LocalDateTime.now();
}
