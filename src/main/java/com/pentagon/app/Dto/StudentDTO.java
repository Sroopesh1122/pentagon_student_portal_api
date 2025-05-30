package com.pentagon.app.Dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StudentDTO {


	    private Integer Id;
	    private String studentId;
	    private String name;
	    private String gender;
	    private String email;
	    private String password;
	    private String mobile;
	    private String stack;
	    private String typeOfAdmission; // paid or CSR
	    private String mode; // offline, online
	    private String address;
	    private String college;
	    private String qualification;
	    private Integer yearOfPassout;
	    private Double perecentage;
	    private String stream;
	    private String objective;
	    private String skills;
	    private String internship;
	    private String projects;
	    private Double mockRating;
	    private EnrollmentStatus status = EnrollmentStatus.PENDING;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;

	    public enum EnrollmentStatus {
	        PENDING,
	        ACTIVE,
	        DISABLED,
	        COMPLETED,
	        DROPPED,
	        PLACED
	

       }
}
