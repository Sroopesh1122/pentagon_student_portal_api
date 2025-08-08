package com.pentagon.app.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(BlockedException.class)
    public ResponseEntity<Map<String, Object>> handleBlockException(BlockedException exception) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Blocked Exception");
        errorResponse.put("error", exception.getMessage());
        errorResponse.put("isBlocked", true);
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status",exception.getHttpStatus().toString());
        return new ResponseEntity<Map<String,Object>>(errorResponse, exception.getHttpStatus());
    }
		
	
	 @ExceptionHandler(TestimonialException.class)
	    public ResponseEntity<Map<String, Object>> handleTestimonialException(TestimonialException testimonialException) {
	        Map<String,Object> errorResponse = new HashMap<>();
	        errorResponse.put("status", "failure");
	        errorResponse.put("type","TEstimonial Exception");
	        errorResponse.put("error", testimonialException.getMessage());
	        errorResponse.put("localTime", LocalDateTime.now());
	        errorResponse.put("status",testimonialException.getHttpStatus().toString());
	        return new ResponseEntity<Map<String,Object>>(errorResponse, testimonialException.getHttpStatus());
	    }
	 @ExceptionHandler(InvalidDataException.class)
	    public ResponseEntity<Map<String, Object>> handleInvalidDataException(InvalidDataException invalidDataException) {
	        Map<String,Object> errorResponse = new HashMap<>();
	        errorResponse.put("status", "failure");
	        errorResponse.put("type","Invalid Input Exception");
	        errorResponse.put("error", invalidDataException.getMessage());
	        errorResponse.put("localTime", LocalDateTime.now());
	        errorResponse.put("status", invalidDataException.getHttpStatus().toString());
	        return new ResponseEntity<Map<String,Object>>(errorResponse, invalidDataException.getHttpStatus());
	    }
	 
	 @ExceptionHandler(MockException.class)
	    public ResponseEntity<Map<String, Object>> handleMockException(MockException mockException) {
	        Map<String,Object> errorResponse = new HashMap<>();
	        errorResponse.put("status", "failure");
	        errorResponse.put("type","Mock Exception");
	        errorResponse.put("error", mockException.getMessage());
	        errorResponse.put("localTime", LocalDateTime.now());
	        errorResponse.put("status", mockException.getHttpStatus().toString());
	        return new ResponseEntity<Map<String,Object>>(errorResponse, mockException.getHttpStatus());
	    }
	 
	 @ExceptionHandler(AnnouncementExpection.class)
	    public ResponseEntity<Map<String, Object>> handleAnnouncementExpection(AnnouncementExpection announcementExpection) {
	        Map<String,Object> errorResponse = new HashMap<>();
	        errorResponse.put("status", "failure");
	        errorResponse.put("type","Announcement Exception");
	        errorResponse.put("error", announcementExpection.getMessage());
	        errorResponse.put("localTime", LocalDateTime.now());
	        errorResponse.put("status", announcementExpection.getHttpStatus().toString());
	        return new ResponseEntity<Map<String,Object>>(errorResponse, announcementExpection.getHttpStatus());
	    }


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
    @ExceptionHandler(BatchException.class)
    public ResponseEntity<Map<String, Object>> handleBatchException(BatchException batchException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Batch Exception");
        errorResponse.put("error", batchException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", batchException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, batchException.getHttpStatus());
    }
    @ExceptionHandler(BatchTechTrainerException.class)
    public ResponseEntity<Map<String, Object>> handleBatchTechTrainerException(BatchTechTrainerException batchTechTrainerException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Batch Tech Trainer Exception");
        errorResponse.put("error", batchTechTrainerException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", batchTechTrainerException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, batchTechTrainerException.getHttpStatus());
    }
    @ExceptionHandler(MockRatingsException.class)
    public ResponseEntity<Map<String, Object>> handleMockRatingsException(MockRatingsException mockRatingsException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Mock Ratings Exception");
        errorResponse.put("error", mockRatingsException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", mockRatingsException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, mockRatingsException.getHttpStatus());
    }
    @ExceptionHandler(StackException.class)
    public ResponseEntity<Map<String, Object>> handleStackException(StackException stackException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Stack Exception");
        errorResponse.put("error", stackException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", stackException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, stackException.getHttpStatus());
    }
    @ExceptionHandler(TechnologyException.class)
    public ResponseEntity<Map<String, Object>> handleTechnologyException(TechnologyException technologyException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Technology Exception");
        errorResponse.put("error", technologyException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", technologyException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, technologyException.getHttpStatus());
    }
    @ExceptionHandler(StudentAdminException.class)
    public ResponseEntity<Map<String, Object>> handleStudentAdminException(StudentAdminException studentAdminException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Student Admin Exception");
        errorResponse.put("error", studentAdminException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", studentAdminException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, studentAdminException.getHttpStatus());
    }
    
    @ExceptionHandler(ProgramHeadException.class)
    public ResponseEntity<Map<String, Object>> handleProgramException(ProgramHeadException programHeadException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Program Head Exception");
        errorResponse.put("error", programHeadException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", programHeadException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, programHeadException.getHttpStatus());
    }
    @ExceptionHandler(StudentJdApplcationException.class)
    public ResponseEntity<Map<String, Object>> handleStudentJdApplcationException(StudentJdApplcationException studentJdApplcationException) {
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type","Stuent Jd Applcation Exception");
        errorResponse.put("error", studentJdApplcationException.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("status", studentJdApplcationException.getHttpStatus().toString());
        return new ResponseEntity<>(errorResponse, studentJdApplcationException.getHttpStatus());
    }
    
}