package com.nixora.loan.chat.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.ThinkingConfig;
import com.google.genai.types.ThinkingLevel;
import com.nixora.auth.entities.User;
import com.nixora.loan.document.entities.LoanDocument;
import com.nixora.loan.document.repositories.LoanDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanChatService {

    private final LoanDocumentRepository repository;

    @Value("${google.cloud.project-id}")
    private String projectId;

    public String askQuestion(UUID loanId, User user, String question) throws Exception {

        LoanDocument doc = repository.findAccessibleLoan(loanId, user)
                .orElseThrow(() -> new RuntimeException("Loan not found or access denied"));

        String context = doc.getExtractedText();

        String keyPath = "/etc/secrets/google-key.json";

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));


        Client client = Client.builder()
                .project(projectId)
                .location("global")
                .credentials(credentials)
                .vertexAI(true)
                .build();

        GenerateContentConfig config = GenerateContentConfig.builder()
                .thinkingConfig(ThinkingConfig.builder()
                        .thinkingLevel("high")
                        .build())
                .build();


            String prompt = String.format(
                    "You are an expert loan assistant. Answer the user's question strictly using the provided loan document context. " +
                            "If the information is not in the text, say you don't know.\n\n" +
                            "### DOCUMENT CONTEXT:\n%s\n\n" +
                            "### USER QUESTION:\n%s",
                    context, question
            );


        GenerateContentResponse response = client.models.generateContent(
                "gemini-3-pro-preview",
                prompt,
                config
        );
        return response.text();
        }
    }
