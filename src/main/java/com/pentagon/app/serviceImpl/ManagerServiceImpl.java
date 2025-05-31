package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Manager;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.request.AddExecutiveRequest;
import com.pentagon.app.request.AddTrainerRequest;
import com.pentagon.app.request.ManagerLoginRequest;
import com.pentagon.app.request.UpdateManagerRequest;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.service.TrainerService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ExecutiveService executiveService;
	
	@Autowired
	private TrainerService trainerService;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ActivityLogService activityLogService;
	
	
	/* Performs the actual update of manager details in the database,
	* manages timestamps, and handles exceptions during save operation.
	* Rolls back if an error occurs.
	*/
	@Transactional
	@Override
	public void updateManagerDetails(CustomUserDetails managerDetails, UpdateManagerRequest request) {
	    try {
	        Manager manager = managerDetails.getManager();

	        manager.setName(request.getName());
	        manager.setEmail(request.getEmail());
	        manager.setMobile(request.getMobile());

	        if (request.getPassword() != null && !request.getPassword().isBlank()) {
	            try {
	                String hashedPassword = passwordEncoder.encode(request.getPassword());
	                manager.setPassword(hashedPassword);
	            } catch (Exception e) {
	                throw new ManagerException("Password encoding failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	        }

	        manager.setUpdatedAt(LocalDateTime.now());
	        managerRepository.save(manager);

	        activityLogService.log(
	            manager.getEmail(),
	            manager.getManagerId(),
	            "MANAGER",
	            "Manager with ID " + manager.getManagerId() + " updated their own profile details"
	        );

	    } catch (Exception e) {
	        throw new ManagerException("Failed to update Manager: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

//	@Override
//	public Manager updateManager(Manager manager) {
//		// TODO Auto-generated method stub
//		try {
//			manager.setUpdatedAt(LocalDateTime.now());
//			return managerRepository.save(manager);
//			 
//		}
//		catch (Exception e) {
//	        throw new ManagerException("Failed to update Manager: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
//	}
	
	@Transactional
	@Override
	public void addExecutive(CustomUserDetails managerDetails, @Valid AddExecutiveRequest newExecutive) {
		try {
			executiveService.addExecutive(managerDetails, newExecutive);
		}catch (Exception e) {
			throw  new ManagerException("Failed to add executive: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@Transactional
	@Override
	public void addTrainer(CustomUserDetails managerDetails, @Valid AddTrainerRequest newTrainerRequest) {
		try {
			trainerService.addTrainer(managerDetails, newTrainerRequest);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	
	@Override
	public String loginWithPassword(ManagerLoginRequest managerLoginRequest) 
	{
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
	public ProfileResponse getProfile(Manager manager) {
		ProfileResponse result= new ProfileResponse();
		result.setUniqueId(manager.getManagerId());
		result.setName(manager.getName());
		result.setEmail(manager.getEmail());
		result.setMobile(manager.getMobile());
		return result;
	}
	
	
	@Override
	public Page<Manager> findAll(String q , Pageable pageable) {
		return managerRepository.findAll(q, pageable);
	}

	
	@Override
	@Transactional
	public Manager addManager(Manager newManager) {
		
		try {
			newManager.setCreatedAt(LocalDateTime.now());
			Manager manager=managerRepository.save(newManager);
			return manager;

			} catch (Exception e) {
			throw new AdminException("Failed to add Manager: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@Override
	public void disableManagerByUniqueId(String managerId) {
		// TODO Auto-generated method stub
		Manager manager = managerRepository.findByManagerId(managerId).orElseThrow(
				() -> new ExecutiveException("Manager not found with ID: " + managerId, HttpStatus.NOT_FOUND));

		manager.setActive(!manager.isActive());
		manager.setUpdatedAt(LocalDateTime.now());
		managerRepository.save(manager);
	}

	@Override
	public boolean getManagerByEmail(String email) {
		Optional<Manager> manager=managerRepository.findByEmail(email);
		if(manager.isPresent())
			return false;
		return true;
	}


}
