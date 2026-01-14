package com.nixora.loan.document.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.documentai.v1.*;
import com.google.cloud.storage.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
public class GoogleDocAIExtractor {

    @Value("${google.cloud.project-id}")
    private String projectId;

    @Value("${google.cloud.location}")
    private String location;

    @Value("${google.cloud.documentai.processor-id}")
    private String processorId;

    @Value("${google.cloud.storage.bucket-name}")
    private String bucketName;

    public String extractText(MultipartFile file) throws Exception {
        String keyPath = "/etc/secrets/google-key.json";
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();

        String uniqueId = UUID.randomUUID().toString();
        String gcsInputPath = "input/" + uniqueId + ".pdf";
        String gcsOutputPath = "output/" + uniqueId + "/";

        storage.create(BlobInfo.newBuilder(bucketName, gcsInputPath)
                .setContentType("application/pdf").build(), file.getBytes());

        String endpoint = String.format("%s-documentai.googleapis.com:443", location);
        DocumentProcessorServiceSettings settings = DocumentProcessorServiceSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .setEndpoint(endpoint)
                .build();

        StringBuilder fullText = new StringBuilder();

        try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create(settings)) {
            String processorName = ProcessorName.of(projectId, location, processorId).toString();

            BatchProcessRequest request = BatchProcessRequest.newBuilder()
                    .setName(processorName)
                    .setInputDocuments(BatchDocumentsInputConfig.newBuilder()
                            .setGcsDocuments(GcsDocuments.newBuilder()
                                    .addDocuments(GcsDocument.newBuilder()
                                            .setGcsUri("gs://" + bucketName + "/" + gcsInputPath)
                                            .setMimeType("application/pdf").build()).build()).build())
                    .setDocumentOutputConfig(DocumentOutputConfig.newBuilder()
                            .setGcsOutputConfig(DocumentOutputConfig.GcsOutputConfig.newBuilder()
                                    .setGcsUri("gs://" + bucketName + "/" + gcsOutputPath).build()).build())
                    .build();

            log.info("Starting Document AI batch process...");
            client.batchProcessDocumentsAsync(request).get();
            log.info("Batch process complete. Streaming results from GCS...");

            JsonFactory factory = new JsonFactory();

            for (Blob blob : storage.list(bucketName, Storage.BlobListOption.prefix(gcsOutputPath)).iterateAll()) {
                if (blob.getName().endsWith(".json")) {
                    log.info("Streaming shard: {}", blob.getName());

                    try (InputStream is = Channels.newInputStream(blob.reader());
                         JsonParser jParser = factory.createParser(is)) {

                        while (jParser.nextToken() != null) {
                            String fieldname = jParser.getCurrentName();
                            if ("text".equals(fieldname)) {
                                jParser.nextToken();
                                String shardText = jParser.getText();
                                if (shardText != null) {
                                    fullText.append(shardText);
                                }
                                jParser.skipChildren();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error streaming shard {}: {}", blob.getName(), e.getMessage());
                    }
                }
            }
        }

        if (fullText.length() == 0) {
            throw new RuntimeException("No text found in shards");
        }

        return fullText.toString();
    }
}