package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.request.TrainerLoginRequest;
import com.pentagon.app.response.ProfileResponceDto;

import jakarta.validation.Valid;

public interface TrainerService {

	public Trainer updateTrainer(Trainer trainer);
	
	public Student addStudent(Student student);
	
	public List<Student> viewStudentsBasedOnStack(String stack);
	
	public boolean addMockRating(String studentId, Double mockRating);
	
	public void disableStudentByUniqueId(String studentId);

	String loginWithPassword(TrainerLoginRequest trainerLoginRequest);

	public Boolean verifyOtp(OtpVerificationRequest otpVerificationRequest);

	public ProfileResponceDto getProfile(Trainer trainer);
}
