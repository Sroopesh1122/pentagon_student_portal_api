package com.pentagon.app.restController;

import com.pentagon.app.exception.ProfileException;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

    private final CloudinaryServiceImp cloudinaryService;

    @Autowired
    public CloudinaryController(CloudinaryServiceImp cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/public/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = cloudinaryService.uploadFile(file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/public/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("publicId") String publicId) {
        boolean deleted = cloudinaryService.deleteFile(publicId);
        if (deleted) {
            return ResponseEntity.ok("File deleted successfully");
        } else {
            throw new ProfileException("Failed to delete file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
