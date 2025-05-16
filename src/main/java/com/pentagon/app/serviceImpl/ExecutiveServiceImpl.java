package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.service.ExecutiveService;

@Service
public class ExecutiveServiceImpl implements ExecutiveService {

	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	@Override
	public Executive login(String email, String otp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateExecutive(Executive executive) {
		// TODO Auto-generated method stub
		try {
			executive.setUpdatedAt(LocalDateTime.now());
			executiveRepository.save(executive);
			return true;
		}
		catch (Exception e) {
	        throw new ExecutiveException("Failed to update Executive: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
    
	@Override
	public boolean addJobDescription(JobDescription jobDescription) {
		// TODO Auto-generated method stub
		try {
			jobDescription.setCreatedAt(LocalDateTime.now());
			jobDescriptionRepository.save(jobDescription);
			return true;
		}
		catch(Exception e) {
			throw new JobDescriptionException("Failed to Add Job Description: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
	@Override
	public boolean updateJobDescription(JobDescription jobDescription) {
		// TODO Auto-generated method stub
		try {
			jobDescription.setUpdatedAt(LocalDateTime.now());
			jobDescriptionRepository.save(jobDescription);
			return true;
		}
		catch(Exception e) {
			throw new JobDescriptionException("Failed to Update Job Description: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
