package com.ctm.contactManager.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

        String uploadImage(MultipartFile Image, String filename);

        String getUrlFromPublicId(String publicId);       
}