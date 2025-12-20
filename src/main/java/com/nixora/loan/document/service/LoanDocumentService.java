package com.nixora.loan.document.service;

import com.nixora.auth.entities.User;
import com.nixora.loan.cloudinary.service.DocumentStorageService;
import com.nixora.loan.document.dto.UploadLoanDocumentResponse;
import com.nixora.loan.document.entities.DocumentStatus;
import com.nixora.loan.document.entities.LoanDocument;
import com.nixora.loan.document.extractor.DocumentTextExtractor;
import com.nixora.loan.document.repositories.LoanDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanDocumentService {

    private final LoanDocumentRepository repository;
    private final DocumentTextExtractor textExtractor;
    private final DocumentStorageService storageService;

    @Transactional
    public UploadLoanDocumentResponse uploadDocument(
            MultipartFile file,
            User user
    ) {

        String fileUrl = storageService.store(file);

        String extractedText = textExtractor.extractText(file);

        LoanDocument document = new LoanDocument();
        document.setOriginalFileName(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setStorageKey(fileUrl);
        document.setExtractedText(extractedText);
        document.setStatus(DocumentStatus.TEXT_EXTRACTED);
        document.setUploadedAt(LocalDateTime.now());
        document.setUploadedBy(user);

        repository.save(document);

        return new UploadLoanDocumentResponse(
                document.getId(),
                document.getStatus(),
                "Document uploaded to Cloudinary",
                extractedText
        );
    }
}
