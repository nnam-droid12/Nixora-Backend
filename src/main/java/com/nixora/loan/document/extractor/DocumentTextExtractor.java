package com.nixora.loan.document.extractor;

import com.nixora.loan.document.exception.DocumentExtractionException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
public class DocumentTextExtractor {

    public String extractText(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {

            AutoDetectParser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();

            parser.parse(inputStream, handler, metadata);

            return handler.toString();

        } catch (Exception e) {
            throw new DocumentExtractionException("Failed to extract text", e);
        }
    }
}
