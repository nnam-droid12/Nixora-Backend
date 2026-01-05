package com.nixora.loan.document.service;

import com.nixora.auth.entities.User;
import com.nixora.loan.LoanQueryEngine.entities.LoanSnapshotEntity;
import com.nixora.loan.LoanQueryEngine.repositories.LoanSnapshotRepository;
import com.nixora.loan.LoanQueryEngine.service.LoanSnapshotBuilder;
import com.nixora.loan.LoanSchedule.service.LoanScheduleService;
import com.nixora.loan.PushNotification.service.PushNotificationService;
import com.nixora.loan.cloudinary.service.DocumentStorageService;
import com.nixora.loan.document.dto.DeleteLoanResponse;
import com.nixora.loan.document.dto.LmaLoanData;
import com.nixora.loan.document.dto.LoanDocumentResponse;
import com.nixora.loan.document.dto.UploadLoanDocumentResponse;
import com.nixora.loan.document.entities.DocumentStatus;
import com.nixora.loan.document.entities.LoanDocument;
import com.nixora.loan.document.entities.UpdateLoanFieldRequest;
import com.nixora.loan.document.exception.FileAlreadyExistsException;
import com.nixora.loan.document.exception.FileTooLargeException;
import com.nixora.loan.document.exception.InvalidFileException;
import com.nixora.loan.document.exception.ResourceNotFoundException;
import com.nixora.loan.document.extractor.LoanApiMapper;
import com.nixora.loan.document.repositories.LoanDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanDocumentService {

    private final LoanDocumentRepository repository;
    private final DocumentStorageService storageService;
    private final LoanSnapshotRepository snapshotRepository;
    private final LoanSnapshotBuilder snapshotBuilder;
    private final LoanApiMapper apiMapper;
    private final LoanScheduleService scheduleService;
    private final LoanFieldUpdater fieldUpdater;
    private final PushNotificationService pushService;

    private final GoogleDocAIExtractor googleDocAi;
    private final GeminiLmaExtractor geminiExtractor;




    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB

    private static final List<String> ALLOWED_TYPES = List.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );


    @Transactional
    public UploadLoanDocumentResponse uploadDocument(MultipartFile file, User user) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("No document selected for upload");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileTooLargeException("File size exceeds 20MB limit");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new InvalidFileException("Unsupported document type");
        }

        if (repository.existsByOriginalFileNameAndUploadedBy(file.getOriginalFilename(), user)) {
            throw new FileAlreadyExistsException("You already uploaded this document");
        }


        String documentUrl = storageService.store(file);

        try {

            String extractedText = googleDocAi.extractText(file);

            LmaLoanData loanData = geminiExtractor.extractStructuredData(extractedText);

            UUID loanId = UUID.randomUUID();
            LoanDocument document = new LoanDocument();
            document.setLoanId(loanId);
            document.setUploadedBy(user);
            document.setOriginalFileName(file.getOriginalFilename());
            document.setDocumentUrl(documentUrl);
            document.setExtractedText(extractedText);
            document.setLoanData(loanData);
            document.setStatus(DocumentStatus.TEXT_EXTRACTED);
            document.setUploadedAt(LocalDateTime.now());

            repository.save(document);

            scheduleService.generateAndStore(document);

            LoanSnapshotEntity snapshot = snapshotBuilder.from(document);
            snapshotRepository.save(snapshot);


            pushService.send(user,
                    "Document uploaded",
                    user.getName() + ", you just uploaded a (" +
                            document.getOriginalFileName() + ") on your Nixora dashboard"
            );


            return new UploadLoanDocumentResponse(
                    document.getId(),
                    loanId,
                    documentUrl,
                    apiMapper.toResponse(document),
                    document.getStatus().name(),
                    document.getOriginalFileName()
            );


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    @Transactional(readOnly = true)
    public List<LoanDocumentResponse> getAllLoans(User user) {
        return repository.findAllByUploadedBy(user)
                .stream()
                .map(doc -> new LoanDocumentResponse(
                        doc.getId(),
                        doc.getLoanId(),
                        doc.getDocumentUrl(),
                        apiMapper.toResponse(doc),
                        doc.getStatus().name(),
                        doc.getOriginalFileName()
                ))
                .toList();
    }



    @Transactional(readOnly = true)
    public LoanDocumentResponse getLoanById(UUID loanId, User user) {

        LoanDocument doc = repository.findByLoanIdAndUploadedBy(loanId, user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Loan not found or not owned by you: " + loanId));

        return new LoanDocumentResponse(
                doc.getId(),
                doc.getLoanId(),
                doc.getDocumentUrl(),
                apiMapper.toResponse(doc),
                doc.getStatus().name(),
                doc.getOriginalFileName()
        );
    }


    @Transactional
    public DeleteLoanResponse deleteLoan(UUID loanId, User user) {

        if (!repository.existsByLoanIdAndUploadedBy(loanId, user)) {
            throw new ResourceNotFoundException(
                    "Loan not found or not owned by you: " + loanId
            );
        }

        snapshotRepository.deleteByLoanId(loanId);

        repository.deleteByLoanIdAndUploadedBy(loanId, user);

        return new DeleteLoanResponse(
                loanId,
                "Loan with id " + loanId + " deleted successfully"
        );
    }




    @Transactional
    public LoanDocumentResponse updateField(UUID loanId, User user, UpdateLoanFieldRequest req) {

        LoanDocument doc = repository.findAccessibleLoan(loanId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        LmaLoanData data = doc.getLoanData();

        fieldUpdater.update(data, req.getPath(), req.getValue());

        doc.setLoanData(data);
        doc.setStatus(DocumentStatus.EDITED);

        repository.save(doc);


        snapshotRepository.deleteByLoanId(loanId);
        snapshotRepository.save(snapshotBuilder.from(doc));
        scheduleService.regenerate(doc);

        return new LoanDocumentResponse(
                doc.getId(),
                doc.getLoanId(),
                doc.getDocumentUrl(),
                apiMapper.toResponse(doc),
                doc.getStatus().name(),
                doc.getOriginalFileName()
        );
    }


}
