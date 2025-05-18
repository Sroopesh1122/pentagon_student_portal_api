package com.pentagon.app.service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.requestDTO.ManagerLoginRequest;
import com.pentagon.app.requestDTO.OtpVerificationRequest;

public interface ManagerService {

    public Manager login(String email, String otp);
	
	public boolean updateManager(Manager manager);
	
	public boolean addExecutive(Executive executive);
	
	public boolean addTrainer(Trainer trainer);
	
	public JobDescription acceptJobDescription(String jobDescriptionId);

	Boolean verifyByOtp(OtpVerificationRequest otpVerificationRequest);

	String loginWithPassword(ManagerLoginRequest managerLoginRequest);
}
