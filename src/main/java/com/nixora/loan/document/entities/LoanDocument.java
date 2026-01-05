package com.nixora.loan.document.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nixora.auth.entities.User;
import com.nixora.loan.document.dto.LmaLoanData;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class LoanDocument {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID loanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    private User uploadedBy;

    private String originalFileName;
    private String contentType;
    private Long fileSize;

    private String documentUrl;

    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private String extractedText;


    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private LmaLoanData loanData;



    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;



}
