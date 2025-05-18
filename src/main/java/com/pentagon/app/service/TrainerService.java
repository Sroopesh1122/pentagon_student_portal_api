package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.requestDTO.OtpVerificationRequest;
import com.pentagon.app.requestDTO.TrainerLoginRequest;

public interface TrainerService {

	public boolean updateTrainer(Trainer trainer);
	
	public boolean addStudent(Student student);
	
	public List<Student> viewStudentsBasedOnStack(String stack);
	
	public boolean addMockRating(String studentId, Double mockRating);
	
	public void disableStudentByUniqueId(String studentId);

	Boolean verifyByOtp(OtpVerificationRequest otpVerificationRequest);

	String loginWithPassword(TrainerLoginRequest trainerLoginRequest);
}
