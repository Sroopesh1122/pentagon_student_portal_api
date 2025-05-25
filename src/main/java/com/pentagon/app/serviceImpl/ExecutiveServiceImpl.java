package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.request.ExecutiveLoginRequest;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.OtpService;

@Service
public class ExecutiveServiceImpl implements ExecutiveService {

	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
	public JobDescription updateJobDescription(JobDescription jobDescription) {
		// TODO Auto-generated method stub
		try {
			jobDescription.setUpdatedAt(LocalDateTime.now());
			return jobDescriptionRepository.save(jobDescription);
		}
		catch(Exception e) {
			throw new JobDescriptionException("Failed to Update Job Description: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public String loginWithPassword(ExecutiveLoginRequest executiveLoginRequest) {
		Executive executive = executiveRepository.findByEmail(executiveLoginRequest.getEmail())
				.orElseThrow(()-> new ExecutiveException("Executive not found", HttpStatus.NOT_FOUND));
		if (!passwordEncoder.matches(executiveLoginRequest.getPassword(), executive.getPassword())) {
			throw new ExecutiveException("Inavlid password", HttpStatus.UNAUTHORIZED);
		}
		String otp= otpService.generateOtpAndStore(executiveLoginRequest.getEmail());
		otpService.sendOtpToEmail(executive.getEmail(), otp);
		
		return "OTP sent to registered email";
	}

	

}
