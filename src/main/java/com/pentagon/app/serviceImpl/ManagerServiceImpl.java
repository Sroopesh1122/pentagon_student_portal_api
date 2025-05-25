package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.repository.TrainerRepository;
import com.pentagon.app.request.ManagerLoginRequest;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.response.ProfileResponceDto;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.OtpService;

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	@Autowired
	private TrainerRepository trainerRepository;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Manager login(String email, String otp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Manager updateManager(Manager manager) {
		// TODO Auto-generated method stub
		try {
			manager.setUpdatedAt(LocalDateTime.now());
			return managerRepository.save(manager);
			 
		}
		catch (Exception e) {
	        throw new ManagerException("Failed to update Manager: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public Executive addExecutive(Executive executive) {
		// TODO Auto-generated method stub
		try {
			executive.setCreatedAt(LocalDateTime.now());
			return	executiveRepository.save(executive);
			 
		}
		catch(Exception e) {
			throw new ExecutiveException("Failed to Add Executive: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Override
	public JobDescription acceptJobDescription(String jobDescriptionId) {
		// TODO Auto-generated method stub
		JobDescription jobDescription = jobDescriptionRepository.findByJobDescriptionId(jobDescriptionId)
				.orElseThrow(() -> new JobDescriptionException("Job Description not found with id: " + jobDescriptionId, HttpStatus.NOT_FOUND));
		
		if (jobDescription.isManagerApproval()) {
            throw new JobDescriptionException("Job Description is already approved", HttpStatus.CONFLICT);
        }
		
		jobDescription.setManagerApproval(true);
		jobDescriptionRepository.save(jobDescription);
		
		return jobDescription;
	}

	@Override
	public Trainer addTrainer(Trainer trainer) {
		// TODO Auto-generated method stub
		try {
			trainer.setCreatedAt(LocalDateTime.now());
			return trainerRepository.save(trainer);
			 
		}
		catch(Exception e) {
			throw new TrainerException("Failed to Add Trainer: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Page<Trainer> viewAllTrainers(String stack, String name, String trainerId, Pageable pageable) {
	    try {
	        return trainerRepository.findByFilters(stack, name, trainerId, pageable);
	    } catch (Exception e) {
	        throw new ManagerException("Failed to fetch trainers", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	    
	@Override
	public String loginWithPassword(ManagerLoginRequest managerLoginRequest) {
		Manager manager= managerRepository.findByEmail(managerLoginRequest.getEmail())
				.orElseThrow(()-> new ManagerException("Manager not found", HttpStatus.NOT_FOUND));
		if(!passwordEncoder.matches(managerLoginRequest.getPassword(), manager.getPassword())) {
			throw new ManagerException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		String otp= otpService.generateOtpAndStore(managerLoginRequest.getEmail());
		otpService.sendOtpToEmail(managerLoginRequest.getEmail(), otp);
		
		return "OTP sent to registered email";

	}


	@Override
	public ProfileResponceDto getProfile(Manager manager) {
		ProfileResponceDto result= new ProfileResponceDto();
		result.setUniqueId(manager.getManagerId());
		result.setName(manager.getName());
		result.setEmail(manager.getEmail());
		result.setMobile(manager.getMobile());
		return result;
	}


}
