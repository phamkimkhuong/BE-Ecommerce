package com.backend.productservice.services;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile file);
}
