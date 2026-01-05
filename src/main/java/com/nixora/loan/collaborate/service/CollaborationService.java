package com.nixora.loan.collaborate.service;

import com.nixora.auth.entities.User;
import com.nixora.loan.collaborate.entity.CollaborationInvite;
import com.nixora.loan.collaborate.entity.LoanCollaborator;
import com.nixora.loan.collaborate.repository.CollaborationInviteRepository;
import com.nixora.loan.collaborate.repository.LoanCollaboratorRepository;
import com.nixora.loan.document.entities.LoanDocument;
import com.nixora.loan.document.repositories.LoanDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollaborationService {

    private final CollaborationInviteRepository inviteRepo;
    private final LoanDocumentRepository loanRepo;
    private final LoanCollaboratorRepository collaboratorRepo;

    // Create invite
    @Transactional
    public String createInvite(User owner) {

        CollaborationInvite invite = new CollaborationInvite();
        invite.setToken(UUID.randomUUID().toString());
        invite.setOwner(owner);
        invite.setExpiresAt(LocalDateTime.now().plusDays(2));
        invite.setUsed(false);

        inviteRepo.save(invite);

        return "https://nixora.onrender.com/invite/" + invite.getToken();
    }


    @Transactional
    public void acceptInvite(String token, User user) {

        CollaborationInvite invite = inviteRepo
                .findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invite"));

        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invite expired");
        }

        User owner = invite.getOwner();


        List<LoanDocument> loans = loanRepo.findAllByUploadedBy(owner);

        for (LoanDocument loan : loans) {

            if (!collaboratorRepo.existsByLoanAndUser(loan, user)) {
                LoanCollaborator c = new LoanCollaborator();
                c.setLoan(loan);
                c.setUser(user);
                c.setCanEdit(true);
                c.setGrantedAt(LocalDateTime.now());

                collaboratorRepo.save(c);
            }
        }

        invite.setUsed(true);
        inviteRepo.save(invite);
    }
}

