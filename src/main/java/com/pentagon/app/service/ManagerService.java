package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.requestDTO.ManagerLoginRequest;
import com.pentagon.app.requestDTO.OtpVerificationRequest;
import com.pentagon.app.response.ProfileResponceDto;

public interface ManagerService {

    public Manager login(String email, String otp);
	
	public Manager updateManager(Manager manager);
	
	public Executive addExecutive(Executive executive);
	
	public Trainer addTrainer(Trainer trainer);
	
	public JobDescription acceptJobDescription(String jobDescriptionId);

	Boolean verifyOtp(OtpVerificationRequest otpVerificationRequest);

	String loginWithPassword(ManagerLoginRequest managerLoginRequest);
	
	Page<Trainer> viewAllTrainers(String stack, String name, String trainerId, Pageable pageable);

	public ProfileResponceDto getProfile(Manager manager);



}
