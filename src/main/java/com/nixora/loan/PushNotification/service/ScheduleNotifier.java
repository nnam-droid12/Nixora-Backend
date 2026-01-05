package com.nixora.loan.PushNotification.service;

import com.nixora.loan.LoanSchedule.entity.LoanScheduleEvent;
import com.nixora.loan.LoanSchedule.repository.LoanScheduleRepository;
import com.nixora.loan.document.entities.LoanDocument;
import com.nixora.loan.document.repositories.LoanDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleNotifier {

    private final LoanScheduleRepository repo;
    private final LoanDocumentRepository docRepo;
    private final PushNotificationService pushService;

    @Scheduled(cron = "0 0 * * * *") // every hour
    public void checkEvents() {
        LocalDate today = LocalDate.now();

        List<LoanScheduleEvent> events = repo.findByEventDate(today);

        for (LoanScheduleEvent e : events) {

            LoanDocument doc = docRepo.findByLoanId(e.getLoanId()).orElse(null);
            if (doc == null) continue;

            pushService.send(
                    doc.getUploadedBy(),
                    "Loan reminder",
                    e.getDescription() + " for " + doc.getOriginalFileName()
            );
        }
    }
}
