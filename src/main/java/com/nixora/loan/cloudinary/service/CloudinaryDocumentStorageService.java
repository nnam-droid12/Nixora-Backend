package com.nixora.loan.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.nixora.loan.document.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryDocumentStorageService implements DocumentStorageService {

    private final Cloudinary cloudinary;

    @Override
    public String store(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("resource_type", "raw")
            );
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new StorageException("Failed to store document in cloudinary", e);
        }
    }
}

