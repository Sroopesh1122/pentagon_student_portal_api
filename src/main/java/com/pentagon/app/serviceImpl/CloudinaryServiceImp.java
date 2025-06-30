package com.pentagon.app.serviceImpl;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pentagon.app.exception.ProfileException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImp {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImp( 
    		@Value("${CLOUDINARY_CLOUD_NAME}") String cloudName,
    		@Value("${CLOUDINARY_API_KEY}") String apiKey, 
    		@Value("${CLOUDINARY_SECRET_KEY}") String apiSecret) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        this.cloudinary = new Cloudinary(config);
    }

    public Map<String, Object> uploadFile(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProfileException("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean deleteFile(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProfileException("Failed to delete file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}