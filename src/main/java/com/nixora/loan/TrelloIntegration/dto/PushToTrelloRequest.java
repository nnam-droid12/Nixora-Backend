package com.nixora.loan.TrelloIntegration.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PushToTrelloRequest {
    private UUID loanId;
    private String label;
    private String value;
    private String listId;
}

