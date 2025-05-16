package com.pentagon.app.service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;

public interface ManagerService {

    public Manager login(String email, String otp);
	
	public boolean updateManager(Manager manager);
	
	public boolean addExecutive(Executive executive);
	
	public boolean addTrainer(Trainer trainer);
	
	public JobDescription acceptJobDescription(String jobDescriptionId);
}
