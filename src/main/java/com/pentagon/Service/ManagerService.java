package com.pentagon.Service;

import com.pentagon.entity.Executive;
import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Manager;

public interface ManagerService {

    public Manager login(String email, String otp);
	
	public boolean updateManager(Manager manager);
	
	public Executive addExecutive(Executive executive); 
	
	public boolean addJobDescription(JobDescription jobdescription);
	
	public boolean acceptJobDescription(Integer jobDescriptionId);
}
