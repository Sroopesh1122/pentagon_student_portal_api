package com.pentagon.app.service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;

public interface ExecutiveService {
 
	public Executive login(String email, String otp);
	
	public boolean updateExecutive(Executive executive);
	
	public boolean addJobDescription(JobDescription jobDescription);
	
	public boolean updateJobDescription(JobDescription jobDescription);
	
	
}
