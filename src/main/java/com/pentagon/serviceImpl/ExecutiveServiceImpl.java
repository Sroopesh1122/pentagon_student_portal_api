package com.pentagon.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.Repository.ExecutiveRepository;
import com.pentagon.entity.Executive;
import com.pentagon.entity.JobDescription;
import com.pentagon.service.ExecutiveService;

@Service
public class ExecutiveServiceImpl implements ExecutiveService {

	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Override
	public Executive login(String email, String otp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateExecutive(Executive executive) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addJobDescription(JobDescription jobdescription) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateJobDescription(JobDescription jobdescription) {
		// TODO Auto-generated method stub
		return false;
	}

}
