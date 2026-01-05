package com.nixora.loan.collaborate.repository;

import com.nixora.loan.collaborate.entity.CollaborationInvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CollaborationInviteRepository
        extends JpaRepository<CollaborationInvite, UUID> {

    Optional<CollaborationInvite> findByTokenAndUsedFalse(String token);
}
