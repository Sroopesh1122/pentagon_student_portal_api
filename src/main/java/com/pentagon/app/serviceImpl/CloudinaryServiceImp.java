package com.pentagon.app.serviceImpl;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pentagon.app.exception.ProfileException;
import com.pentagon.app.utils.AppProperties;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class CloudinaryServiceImp {

    private static final Cloudinary cloudinary = initializeCloudinary();

    private static Cloudinary initializeCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", AppProperties.CLOUDINARY_CLOUD_NAME);
        config.put("api_key", AppProperties.CLOUDINARY_API_KEY);
        config.put("api_secret", AppProperties.CLOUDINARY_SECRET_KEY);
        return new Cloudinary(config);
    }

    public static Map<String, Object> uploadFile(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProfileException("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static boolean deleteFile(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProfileException("Failed to delete file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}