package com.nixora.loan.TrelloIntegration.repository;

import com.nixora.auth.entities.User;
import com.nixora.loan.TrelloIntegration.entity.TrelloUserConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrelloUserConfigRepository
        extends JpaRepository<TrelloUserConfig, UUID> {
    Optional<TrelloUserConfig> findByUser(User user);
}

