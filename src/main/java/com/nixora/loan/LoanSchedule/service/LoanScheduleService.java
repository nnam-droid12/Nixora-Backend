package com.nixora.loan.LoanSchedule.service;


import com.nixora.auth.entities.User;
import com.nixora.loan.LoanSchedule.repository.LoanScheduleRepository;
import com.nixora.loan.PushNotification.service.PushNotificationService;
import com.nixora.loan.document.entities.LoanDocument;
import com.nixora.loan.document.repositories.LoanDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanScheduleService {

    private final LoanScheduleGenerator generator;
    private final LoanScheduleRepository repo;

    private final LoanDocumentRepository docRepo;
    private final PushNotificationService pushService;

    public LoanScheduleService(LoanScheduleGenerator g,
                               LoanScheduleRepository r, LoanDocumentRepository docRepo, PushNotificationService pushService) {
        this.generator = g;
        this.repo = r;
        this.docRepo = docRepo;
        this.pushService = pushService;
    }

    @Transactional
    public void generateAndStore(LoanDocument doc) {

        var events = generator.generate(doc);

        User user = doc.getUploadedBy();

        pushService.send(user,
                "Loan schedule created",
                "Your loan schedule has been generated for " +
                        doc.getOriginalFileName()
        );

        repo.saveAll(events);
    }



    @Transactional
    public void regenerate(LoanDocument doc) {
        repo.deleteByLoanId(doc.getLoanId());
        generateAndStore(doc);
    }
}
