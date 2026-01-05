package com.nixora.loan.GoogleCalendarIntegration.repository;

import com.nixora.loan.GoogleCalendarIntegration.entity.GoogleOAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GoogleOAuthRepository extends JpaRepository<GoogleOAuthToken, Integer> {
}

