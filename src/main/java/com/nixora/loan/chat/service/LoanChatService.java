package com.nixora.loan.chat.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
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

        try (VertexAI vertexAI = new VertexAI.Builder()
                .setProjectId(projectId)
                .setLocation("us-central1")
                .setCredentials(credentials)
                .build()) {

            GenerativeModel model = new GenerativeModel("gemini-2.0-flash-001", vertexAI);

            // 2. Build a "Grounded" prompt
            String prompt = String.format(
                    "You are an expert loan assistant. Answer the user's question strictly using the provided loan document context. " +
                            "If the information is not in the text, say you don't know.\n\n" +
                            "### DOCUMENT CONTEXT:\n%s\n\n" +
                            "### USER QUESTION:\n%s",
                    context, question
            );

            GenerateContentResponse response = model.generateContent(prompt);
            return ResponseHandler.getText(response);
        }
    }
}