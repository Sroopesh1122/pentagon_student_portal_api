package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.request.ManagerLoginRequest;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.OtpService;

import jakarta.transaction.Transactional;

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	@Autowired
	private ExecutiveRepository executiveRepository;
	
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
	public Manager getManagerByEmail(String email) {
		Optional<Manager> manager=managerRepository.findByEmail(email);
		if(manager.isPresent())
			return manager.get();
		return null;
	}
	
	
	@Override
	public Manager getManagerById(String managerId) {
		return managerRepository.findByManagerId(managerId).orElse(null);
	}
	
	
	@Override
	public Object getManagersJdDetails(String managetId) {
		
		Map<String , Long> jdDetails = new HashMap<>();
		jdDetails.put("totalJd", jobDescriptionRepository.managerTotalJdCount(managetId, null));
		jdDetails.put("pendingJd", jobDescriptionRepository.managerTotalJdCount(managetId,"pending"));
		jdDetails.put("holdJd", jobDescriptionRepository.managerTotalJdCount(managetId,"hold"));
		jdDetails.put("approved", jobDescriptionRepository.managerTotalJdCount(managetId,"approved"));
		jdDetails.put("rejectedJd", jobDescriptionRepository.managerTotalJdCount(managetId, "rejected"));
		return jdDetails;
	}
	
	@Override
	public Long getAllExecutivesCount(String managerId) {
		return executiveRepository.getTotalExecutiveCountByManagerId(managerId);
	}
	
	@Override
	public Map<String, Long> getManagerJdCountByDate(String managerId ,Integer days) {
		// TODO Auto-generated method stub
		Map<String,Long> jdCounts = new LinkedHashMap<>();
		int i=0;
		while( i <= days)
		{
			String dateStr = LocalDateTime.now().minusDays(i).toLocalDate().toString();
			jdCounts.put(dateStr,jobDescriptionRepository.countJdsByManagerAndDate(managerId, dateStr));
			i++;
		}
		
		return jdCounts;
	}
	
	@Override
	public Page<Executive> getAllExecutives(String managerId, Pageable pageable) {
		return executiveRepository.getAllExecutives(managerId, pageable);
	}

}
