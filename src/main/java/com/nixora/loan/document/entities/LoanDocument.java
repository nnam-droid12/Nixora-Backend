package com.nixora.loan.document.entities;

import com.nixora.auth.entities.User;
import com.nixora.loan.document.entities.DocumentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    @Column(columnDefinition = "TEXT")
    private String extractedText;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private LocalDateTime uploadedAt;

    @Column(nullable = false, unique = true)
    private String storageKey;

    @ManyToOne
    private User uploadedBy;
}
