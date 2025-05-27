package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Executive;

import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.request.ExecutiveLoginRequest;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.response.ProfileResponse;


public interface ExecutiveService {
 
	public Executive login(String email, String otp);
	
	public boolean updateExecutive(Executive executive);
	
	public boolean addJobDescription(JobDescription jobDescription);
	
	public JobDescription updateJobDescription(JobDescription jobDescription);

	String loginWithPassword(ExecutiveLoginRequest executiveLoginRequest);

	public ProfileResponse getProfile(Executive executive);

	public Page<JobDescription> findAllJobDescriptions(String companyName, String stack, String role, Boolean isClosed,
			Integer minYearOfPassing, Integer maxYearOfPassing, String qualification, String stream, Double percentage,
			Pageable pageable);

	
	
}
