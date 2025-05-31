package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.request.AddExecutiveRequest;
import com.pentagon.app.request.ExecutiveLoginRequest;

import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.utils.HtmlContent;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.utils.PasswordGenration;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ExecutiveServiceImpl implements ExecutiveService {

	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailService mailService;
	@Autowired
	private IdGeneration idGeneration;
	
	@Autowired
	private ActivityLogService activityLogService;
	
	@Autowired
	private PasswordGenration passwordGenration;

	@Autowired
	private HtmlContent htmlContentService;
	
	
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

	
	@Transactional
	@Override
	public void addExecutive(CustomUserDetails customUserDetails, @Valid AddExecutiveRequest newExecutive) {
	    if (!isEmailAvailable(newExecutive.getEmail())) {
	        throw new ExecutiveException("Email already exists", HttpStatus.CONFLICT);
	    }
	    try {
	        Executive executive = new Executive();
	        executive.setExecutiveId(idGeneration.generateId("EXECUTIVE"));
	        executive.setName(newExecutive.getName());
	        executive.setEmail(newExecutive.getEmail());
	        executive.setActive(true);
	        executive.setMobile(newExecutive.getMobile());

	        String password = passwordGenration.generateRandomPassword();
	        try {
	            executive.setPassword(passwordEncoder.encode(password));
	        } catch (Exception e) {
	            throw new ExecutiveException("Password encoding failed", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	        
	        executive.setCreatedAt(LocalDateTime.now());
	        executive.setManagerId(newExecutive.getManagerId());
	        executive = executiveRepository.save(executive);
	        
	        String htmlContent = htmlContentService.getHtmlContent(
	            executive.getName(), executive.getEmail(), password
	        );

	        try {
	            activityLogService.log(
	                customUserDetails.getManager().getEmail(),
	                customUserDetails.getManager().getManagerId(),
	                "MANAGER",
	                "Executive with ID " + executive.getExecutiveId() + " added successfully."
	            );
	        } catch (Exception e) {
	            throw new ExecutiveException("Failed to log activity", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	        
	        try {
	            mailService.sendPasswordEmail(
	                executive.getEmail(), 
	                "Welcome to Pentagon – Login Credentials Enclosed",
	                htmlContent
	            );
	        } catch (Exception e) {
	            throw new OtpException("Failed to send password email", HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	    } catch (Exception e) {
	        throw new ExecutiveException("Failed to create executive: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	@Override
	@Transactional
	public Executive addExecutive(Executive newExecutive) {
		try {
			newExecutive.setCreatedAt(LocalDateTime.now());
			return executiveRepository.save(newExecutive);
			
		} catch (Exception e) {
			throw new ExecutiveException("Failed to Add Executive: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void disableExecutiveByUniqueId(String executiveId) {
		// TODO Auto-generated method stub
		Executive executive = executiveRepository.findByExecutiveId(executiveId).orElseThrow(
				() -> new ExecutiveException("Executive not found with ID: " + executiveId, HttpStatus.NOT_FOUND));

		executive.setActive(!executive.isActive());
		executive.setUpdatedAt(LocalDateTime.now());
		executiveRepository.save(executive);
	}

	@Override
	public boolean getExecutiveByEmail(String email) {
		Optional<Executive> executive=executiveRepository.findByEmail(email);
		if(executive.isPresent())
			throw new ExecutiveException("Email is already exists", HttpStatus.CONFLICT);
		return true;
	}

	public boolean isEmailAvailable(String email) {
	    return executiveRepository.findByEmail(email).isEmpty();
	}
	
}

