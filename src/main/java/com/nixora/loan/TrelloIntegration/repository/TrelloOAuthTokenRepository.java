package com.nixora.loan.TrelloIntegration.repository;

import com.nixora.auth.entities.User;
import com.nixora.loan.TrelloIntegration.entity.TrelloOAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrelloOAuthTokenRepository
        extends JpaRepository<TrelloOAuthToken, UUID> {

    Optional<TrelloOAuthToken> findByUser(User user);
}
