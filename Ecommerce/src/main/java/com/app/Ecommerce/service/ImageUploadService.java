package com.app.Ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

	public String uploadImage(MultipartFile file);
}
