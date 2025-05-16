package com.pentagon.service;

import com.pentagon.entity.Executive;
import com.pentagon.entity.JobDescription;

public interface ExecutiveService {
 
	public Executive login(String email, String otp);
	
	public boolean updateExecutive(Executive executive);
	
	public boolean addJobDescription(JobDescription jobDescription);
	
	public boolean updateJobDescription(JobDescription jobDescription);
	
	
}
