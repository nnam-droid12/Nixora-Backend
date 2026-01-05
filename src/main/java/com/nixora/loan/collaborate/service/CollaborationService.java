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

    @Transactional
    public String createInvite(User owner, UUID loanId) {
        // Verify owner actually owns the loan before letting them invite others
        loanRepo.findByLoanIdAndUploadedBy(loanId, owner)
                .orElseThrow(() -> new RuntimeException("You do not own this loan"));

        CollaborationInvite invite = new CollaborationInvite();
        invite.setToken(UUID.randomUUID().toString());
        invite.setOwner(owner);
        invite.setLoanId(loanId); // Set the specific loan
        invite.setExpiresAt(LocalDateTime.now().plusDays(2));
        inviteRepo.save(invite);

        // This is the link you send on WhatsApp
        return "https://nixora.onrender.com/invite/" + invite.getToken();
    }

    @Transactional
    public void acceptInvite(String token, User guestUser) {
        // 1. Find the invite by token
        CollaborationInvite invite = inviteRepo.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new RuntimeException("Invite not found or already used"));

        // 2. Validate expiry
        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invite has expired");
        }

        // 3. Get the specific loan ID stored in the invite
        UUID targetLoanId = invite.getLoanId();
        LoanDocument loan = loanRepo.findByLoanId(targetLoanId)
                .orElseThrow(() -> new RuntimeException("The document no longer exists"));

        // 4. Grant access to this specific loan
        if (!collaboratorRepo.existsByLoanAndUser(loan, guestUser)) {
            LoanCollaborator c = new LoanCollaborator();
            c.setLoan(loan);
            c.setUser(guestUser);
            c.setCanEdit(true);
            c.setGrantedAt(LocalDateTime.now());
            collaboratorRepo.save(c);
        }


        invite.setUsed(true);
        inviteRepo.save(invite);
    }
}