package com.pentagon.app.service;

import com.pentagon.app.entity.Executive;

import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.requestDTO.ExecutiveLoginRequest;
import com.pentagon.app.requestDTO.OtpVerificationRequest;
import com.pentagon.app.requestDTO.updateJobDescription;


public interface ExecutiveService {
 
	public Executive login(String email, String otp);
	
	public boolean updateExecutive(Executive executive);
	
	public boolean addJobDescription(JobDescription jobDescription);
	
	//public boolean updateJobDescription(updateJobDescription updateJd);

	String loginWithPassword(ExecutiveLoginRequest executiveLoginRequest);

	Boolean verifyOtp(OtpVerificationRequest otpVerificationRequest);
	
	
}
