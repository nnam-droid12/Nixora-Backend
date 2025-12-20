package com.nixora.loan.cloudinary.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentStorageService {
    String store(MultipartFile file);
}

