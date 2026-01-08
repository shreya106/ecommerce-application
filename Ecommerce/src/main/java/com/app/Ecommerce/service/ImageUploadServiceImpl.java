package com.app.Ecommerce.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

	private final Cloudinary cloudinary;

    public ImageUploadServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
	@Override
	public String uploadImage(MultipartFile file) {
		try {
			
			System.out.println("Uploading file: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize());
            System.out.println("Content type: " + file.getContentType());
            Map<?, ?> result = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            System.out.println("Cloudinary response: " + result);
            return result.get("secure_url").toString();
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException("Image upload failed", e);
        }
    }
}
