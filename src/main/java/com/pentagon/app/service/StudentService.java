package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Student;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.request.StudentLoginRequest;

import jakarta.validation.Valid;

public interface StudentService {

	public boolean changePassword(String password, String studentId);
	
	public boolean updateStudent(Student student);
	
	public List<JobDescription> viewJobDescriptionBasedOnStack(String stack);

	public String loginWithPassword(StudentLoginRequest studentLoginRequest);

	public Boolean verifyOtp(@Valid OtpVerificationRequest request);
	
	
//	public JobDescription viewJobDescriptionBasedOnStackandMockRating(String stack, String mockRating);
	
}
