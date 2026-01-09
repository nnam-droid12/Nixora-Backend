package com.nixora.loan.LoanSchedule.service;


import com.nixora.auth.entities.User;
import com.nixora.loan.LoanSchedule.repository.LoanScheduleRepository;
import com.nixora.loan.PushNotification.service.PushNotificationService;
import com.nixora.loan.document.entities.LoanDocument;
import com.nixora.loan.document.repositories.LoanDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LoanScheduleService {
    private final LoanScheduleGenerator generator;
    private final LoanScheduleRepository repo;
    private final LoanDocumentRepository docRepo;
    private final PushNotificationService pushService; // Add this

    public LoanScheduleService(LoanScheduleGenerator g,
                               LoanScheduleRepository r,
                               LoanDocumentRepository docRepo,
                               PushNotificationService pushService) {
        this.generator = g;
        this.repo = r;
        this.docRepo = docRepo;
        this.pushService = pushService;
    }

    @Transactional
    public void generateAndStore(LoanDocument doc) {
        var events = generator.generate(doc);
        repo.saveAll(events);

    }

    @Transactional
    public void regenerate(LoanDocument doc) {
        repo.deleteByLoanId(doc.getLoanId());
        generateAndStore(doc);
    }

    public void notifyScheduleCreation(UUID loanId, User user) {
        docRepo.findByLoanId(loanId).ifPresent(doc -> {
            pushService.send(user,
                    "Schedule Created",
                    user.getName() + ", you just created a schedule for " +
                            doc.getOriginalFileName() + ". You can now add it to your Google Calendar!"
            );
        });
    }
}



