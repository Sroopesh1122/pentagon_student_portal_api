package com.pentagon.app.service;

import com.pentagon.app.entity.Executive;

import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.request.ExecutiveLoginRequest;
import com.pentagon.app.request.OtpVerificationRequest;


public interface ExecutiveService {
 
	public Executive login(String email, String otp);
	
	public boolean updateExecutive(Executive executive);
	
	public boolean addJobDescription(JobDescription jobDescription);
	
	public JobDescription updateJobDescription(JobDescription jobDescription);

	String loginWithPassword(ExecutiveLoginRequest executiveLoginRequest);

	Boolean verifyOtp(OtpVerificationRequest otpVerificationRequest);
	
	
}
