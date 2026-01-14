package com.nixora.loan.document.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.ThinkingConfig;
import com.google.genai.types.ThinkingLevel;
import com.nixora.loan.document.dto.LmaLoanData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Collections;

@Service
public class GeminiLmaExtractor {

    @Value("${google.cloud.project-id}")
    private String projectId;

    public LmaLoanData extractStructuredData(String rawText) throws Exception {
        if (rawText == null || rawText.isBlank()) {
            throw new IllegalArgumentException("Input text is null");
        }


        String keyPath = "/etc/secrets/google-key.json";
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));


        Client client = Client.builder()
                .project(projectId)
                .location("global")
                .credentials(credentials)
                .vertexAI(true)
                .build();


        String prompt = "You are a specialized LMA (Loan Market Association) data extraction engine. " +
                "Analyze the provided Credit Agreement text and extract ACTUAL values into a structured JSON format. " +
                "\n\n### CRITICAL RULES:\n" +
                "1. IGNORE definitions. Find actual names/values.\n" +
                "2. Return ONLY valid JSON without markdown formatting.\n\n" +
                "### TARGET JSON SCHEMA:\n" +
                "{\n" +
                "  \"parties\": {\n" +
                "    \"borrower\": \"\", \"parent\": \"\", \"guarantor\": \"\", \"obligors\": [],\n" +
                "    \"lenders\": [], \"originalLenders\": [], \"financeParties\": [],\n" +
                "    \"facilityAgent\": \"\", \"securityAgent\": \"\", \"arranger\": \"\", \"bookrunner\": \"\"\n" +
                "  },\n" +
                "  \"facility\": {\n" +
                "    \"facilityId\": \"\", \"facilityType\": \"\", \"facilityName\": \"\", \"currency\": \"\", \n" +
                "    \"facilityAmount\": \"\", \"commitment\": \"\", \"availabilityPeriod\": \"\", \n" +
                "    \"drawstop\": \"\", \"finalMaturityDate\": \"\", \"repaymentProfile\": \"\", \"purpose\": \"\"\n" +
                "  },\n" +
                "  \"interest\": {\n" +
                "    \"benchmark\": \"\", \"margin\": \"\", \"creditAdjustmentSpread\": \"\", \"interestPeriod\": \"\",\n" +
                "    \"interestPaymentDate\": \"\", \"breakCosts\": \"\", \"defaultInterest\": \"\", \"dayCountFraction\": \"\",\n" +
                "    \"compoundingMethod\": \"\", \"rollover\": \"\"\n" +
                "  },\n" +
                "  \"fees\": {\n" +
                "    \"arrangementFee\": \"\", \"participationFee\": \"\", \"commitmentFee\": \"\",\n" +
                "    \"utilisationFee\": \"\", \"agencyFee\": \"\", \"cancellationFee\": \"\", \"frontEndFee\": \"\"\n" +
                "  },\n" +
                "  \"repayment\": {\n" +
                "    \"repaymentDate\": \"\", \"repaymentInstalments\": \"\", \"amortisationSchedule\": \"\",\n" +
                "    \"balloonPayment\": \"\", \"voluntaryPrepayment\": false, \"mandatoryPrepayment\": false,\n" +
                "    \"changeOfControlPrepayment\": false, \"illegalityPrepayment\": false, \"taxGrossUpPrepayment\": false\n" +
                "  },\n" +
                "  \"prepayment\": {\n" +
                "    \"prepaymentType\": \"\", \"voluntary\": false, \"assetSale\": false, \"insuranceProceeds\": false,\n" +
                "    \"debtIssuance\": false, \"changeOfControl\": false, \"illegality\": false, \"tax\": false,\n" +
                "    \"breakCosts\": \"\", \"prepaymentFee\": \"\"\n" +
                "  },\n" +
                "  \"utilisation\": {\n" +
                "    \"utilisationRequest\": \"\", \"utilisationDate\": \"\", \"utilisationAmount\": \"\",\n" +
                "    \"utilisationCurrency\": \"\", \"utilisationNotice\": \"\", \"multipleDrawings\": false, \"cancelledCommitments\": \"\"\n" +
                "  },\n" +
                "  \"covenants\": {\n" +
                "    \"financial\": [], \"information\": [], \"general\": [],\n" +
                "    \"negativePledge\": [], \"disposals\": [], \"mergers\": [],\n" +
                "    \"changeOfBusiness\": [], \"indebtedness\": [], \"guarantees\": []\n" +
                "  },\n" +
                "  \"eventsOfDefault\": {\n" +
                "    \"nonPayment\": [], \"financialCovenantBreach\": [], \"otherObligationBreach\": [],\n" +
                "    \"misrepresentation\": [], \"crossDefault\": [], \"insolvency\": [],\n" +
                "    \"creditorsProcess\": [], \"unlawfulness\": [], \"changeOfControl\": [],\n" +
                "    \"repudiation\": [], \"auditQualification\": []\n" +
                "  },\n" +
                "  \"governingLaw\": { \"governingLaw\": \"\", \"jurisdiction\": \"\" }\n" +
                "}\n\n" +
                "### DOCUMENT TEXT:\n" + rawText;


        GenerateContentConfig config = GenerateContentConfig.builder()
                .thinkingConfig(ThinkingConfig.builder()
                        .thinkingLevel("low")
                        .build())
                .build();
        try {

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-3-flash-preview",
                    prompt,
                    config
            );


            String jsonOutput = response.text();
            if (jsonOutput == null) throw new RuntimeException("Gemini returned empty response");


            jsonOutput = jsonOutput.replaceAll("```json", "").replaceAll("```", "").trim();

            return new ObjectMapper().readValue(jsonOutput, LmaLoanData.class);
        } catch (Exception e) {
            throw new RuntimeException("Error during LMA extraction: " + e.getMessage(), e);
        }
    }
}