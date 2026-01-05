package com.nixora.loan.document.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "extracted_field")
@Data
public class ExtractedFieldEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID loanId;

    private String fieldKey;

    @Column(columnDefinition = "TEXT")
    private String value;

    private String clause;
    private Integer page;

    @Column(columnDefinition = "TEXT")
    private String textSnippet;

    private Integer startOffset;
    private Integer endOffset;
}
