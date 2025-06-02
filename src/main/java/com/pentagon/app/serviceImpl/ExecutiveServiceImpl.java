package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.exception.ExecutiveException;

import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.request.ExecutiveLoginRequest;

import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.OtpService;

import jakarta.transaction.Transactional;

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
	public Executive updateExecutive(Executive executive) {
		// TODO Auto-generated method stub
		try {
			executive.setUpdatedAt(LocalDateTime.now());
			executive = executiveRepository.save(executive);
			return executive;
		} catch (Exception e) {
			throw new ExecutiveException("Failed to update Executive: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public String loginWithPassword(ExecutiveLoginRequest executiveLoginRequest) {
		Executive executive = executiveRepository.findByEmail(executiveLoginRequest.getEmail())
				.orElseThrow(() -> new ExecutiveException("Executive not found", HttpStatus.NOT_FOUND));
		if (!passwordEncoder.matches(executiveLoginRequest.getPassword(), executive.getPassword())) {
			throw new ExecutiveException("Inavlid password", HttpStatus.UNAUTHORIZED);
		}
		String otp = otpService.generateOtpAndStore(executiveLoginRequest.getEmail());
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
		return executiveRepository.findAll(q, pageable);
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
	public Executive getExecutiveByEmail(String email) {
		Optional<Executive> executive = executiveRepository.findByEmail(email);
		if (executive.isEmpty()) {
			return null;
		}
		return executive.get();

	}

	@Override
	public Executive getExecutiveById(String executiveId) {
		return executiveRepository.findByExecutiveId(executiveId).orElse(null);
	}

	@Override
	public Object getExecutiveJdDetails(String executiveId) {
		Map<String, Long> jdDetails = new HashMap<>();
		jdDetails.put("totalJd", jobDescriptionRepository.executiveTotalJdCount(executiveId, null));
		jdDetails.put("pendingJd", jobDescriptionRepository.executiveTotalJdCount(executiveId, "pending"));
		jdDetails.put("holdJd", jobDescriptionRepository.executiveTotalJdCount(executiveId, "hold"));
		jdDetails.put("approved", jobDescriptionRepository.executiveTotalJdCount(executiveId, "approved"));
		jdDetails.put("rejectedJd", jobDescriptionRepository.executiveTotalJdCount(executiveId, "rejected"));
		return jdDetails;
	}

	
	//PAssing only executive value to get Jd of specific executive
	@Override
	public Page<JobDescription> getRecentJobDescriptions(String executiveId, Integer count) {
		Pageable pageable = PageRequest.of(0, count, Sort.by("created_at").descending());
		return jobDescriptionRepository.findWithFiltersUsingRegex(null, null, null, null, null,
				null,null, null, null, executiveId, null, null,null, pageable);
	}

}
