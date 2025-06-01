package com.pentagon.app.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AdminException.class)
    public ResponseEntity<Map<String, Object>> handleAdminException(AdminException adminException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Admin Exception");
        errorResponse.put("error", adminException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", adminException.getHttpStatus().toString());
        return new ResponseEntity<Map<String,Object>>(errorResponse, adminException.getHttpStatus());
    }
    
    @ExceptionHandler(ExecutiveException.class)
    public ResponseEntity<Map<String, Object>> handleExecutiveException(ExecutiveException executiveException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Executive Exception");
        errorResponse.put("error", executiveException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", executiveException.getHttpStatus().toString());
        return new ResponseEntity<Map<String,Object>>(errorResponse, executiveException.getHttpStatus());
    }
    
    @ExceptionHandler(JobDescriptionException.class)
    public ResponseEntity<Map<String, Object>> handleJobDescriptionException(JobDescriptionException jobDescriptionException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Job Description Exception");
        errorResponse.put("error", jobDescriptionException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", jobDescriptionException.getHttpStatus().toString());
        return new ResponseEntity<Map<String,Object>>(errorResponse, jobDescriptionException.getHttpStatus());
    }
    
    @ExceptionHandler(ManagerException.class)
    public ResponseEntity<Map<String, Object>> handleManagerException(ManagerException managerException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Manager Exception");
        errorResponse.put("error", managerException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", managerException.getHttpStatus().toString());
        return new ResponseEntity<Map<String,Object>>(errorResponse, managerException.getHttpStatus());
    }
    @ExceptionHandler(StudentException.class)
    public ResponseEntity<Map<String, Object>> handleStudentException(StudentException studentException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Student Exception");
        errorResponse.put("error", studentException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", studentException.getHttpStatus().toString());
        return new ResponseEntity<Map<String,Object>>(errorResponse, studentException.getHttpStatus());
    }
    @ExceptionHandler(TrainerException.class)
    public ResponseEntity<Map<String, Object>> handleTrainerException(TrainerException trainerException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Trainer Exception");
        errorResponse.put("error", trainerException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", trainerException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, trainerException.getHttpStatus());
    }
    @ExceptionHandler(OtpException.class)
    public ResponseEntity<Map<String, Object>> handleOtpException(OtpException otpException){
    	Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","OTP Exception");
        errorResponse.put("error", otpException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", otpException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, otpException.getHttpStatus());
    }
    @ExceptionHandler(IdGenerationException.class)
    public ResponseEntity<Map<String, Object>> handleIdException(IdGenerationException idGenerationException){
    	Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","IdGeneration Exception");
        errorResponse.put("error", idGenerationException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", idGenerationException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, idGenerationException.getHttpStatus());
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException userException){
    	Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","User Exception");
        errorResponse.put("error", userException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", userException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, userException.getHttpStatus());
    }
    
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> sesstionExpiredException(ExpiredJwtException sessionException){
    	Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Session Expired");
        errorResponse.put("error", sessionException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("relogin", true);
        errorResponse.put("status", "457");
        return new ResponseEntity<>(errorResponse,HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Validation Exception");
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("errors", ex.getErrors());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", ex.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }
}