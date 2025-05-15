package com.pentagon.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.Repository.ManagerRepository;
import com.pentagon.Service.ManagerService;
import com.pentagon.entity.Executive;
import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Manager;

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private ManagerRepository managerRepository;
	
	@Override
	public Manager login(String email, String otp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateManager(Manager manager) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Executive addExecutive(Executive executive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addJobDescription(JobDescription jobdescription) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean acceptJobDescription(Integer jobDescriptionId) {
		// TODO Auto-generated method stub
		return false;
	}

}
