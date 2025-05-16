package com.pentagon.service;

import com.pentagon.entity.Executive;
import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Manager;
import com.pentagon.entity.Trainer;

public interface ManagerService {

    public Manager login(String email, String otp);
	
	public boolean updateManager(Manager manager);
	
	public boolean addExecutive(Executive executive);
	
	public boolean addTrainer(Trainer trainer);
	
	public boolean addJobDescription(JobDescription jobDescription);
	
	public JobDescription acceptJobDescription(String jobDescriptionId);
}
