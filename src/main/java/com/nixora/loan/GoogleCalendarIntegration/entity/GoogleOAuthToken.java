package com.nixora.loan.GoogleCalendarIntegration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class GoogleOAuthToken {

    @Id
    private Integer userId;

    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiry;
}
