package com.pentagon.app.Dto;

import java.time.LocalDateTime;

import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.Stack;
import lombok.Data;

@Data
public class StudentDTO {


	private String studentId;

    private String name;

    private String gender;

    private String email;

    private String mobile;
    
    private String profileUrl;
    
    private String profilePublicId;
    
    private String whatsappNo;
    
    private String dob;
    
    private String summary;
    
    private String address;
    
    private String state;
    
    private String city;
    
    private String experience;
    
    private String skills;
    
    private String tenthSchool;
    
    private Integer tenthPassOutYear;
    
    private Double tenthPercentage;
    
    private String twelveSchool;
    
    private Integer twelvePassOutYear;
    
    private Double twelvePercentage;
    
    private String gradSchool;
    
    private String gradCourse;
    
    private String gradBranch;
    
    private Double gradPercentage;
    
    private Double gradCgpa;
    
    private Integer gradPassOutYear;

    private String typeOfAdmission; // paid or CSR
    
    
    private String studyMode;
	    private EnrollmentStatus status = EnrollmentStatus.PENDING;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    private Batch batch;
	    private Stack stack;

	    public enum EnrollmentStatus {
	        PENDING,
	        ACTIVE,
	        DISABLED,
	        COMPLETED,
	        DROPPED,
	        PLACED
	

       }
}
