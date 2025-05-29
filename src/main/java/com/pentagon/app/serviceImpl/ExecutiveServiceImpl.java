package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.request.ExecutiveLoginRequest;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.response.ProfileResponse;
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

	@Override
	public ProfileResponse getProfile(Executive executive) {
		// TODO Auto-generated method stub
		ProfileResponse result = new ProfileResponse();
		result.setUniqueId(executive.getExecutiveId());
		result.setName(executive.getName());
		result.setEmail(executive.getEmail());
		result.setMobile(executive.getMobile());
		return result;
	}
	
	
	@Override
	public Page<Executive> getAllExecutives(String q, Pageable pageable) {
		return executiveRepository.findExecutivesByFilters(q, pageable);
	}

	
	
}

	

	

