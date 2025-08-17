package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.entity.Student.EnrollmentStatus;
import com.pentagon.app.exception.StudentException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.repository.BatchTechTrainerRepository;
import com.pentagon.app.repository.StudentRepository;
import com.pentagon.app.repository.TrainerRepository;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.request.TrainerLoginRequest;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.service.TrainerService;

import jakarta.transaction.Transactional;

@Service
public class TrainerServiceImpl implements TrainerService {

	@Autowired
	private TrainerRepository trainerRepository;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	@Override
	public Trainer updateTrainer(Trainer trainer) {
		try {
			trainer.setUpdatedAt(LocalDateTime.now());
			return trainerRepository.save(trainer);
			 
		}
		catch (Exception e) {
	        throw new TrainerException("Failed to update Trainer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	@Override
	public String loginWithPassword(TrainerLoginRequest trainerLoginRequest) {
		Trainer trainer = trainerRepository.findByEmail(trainerLoginRequest.getEmail())
				.orElseThrow(()-> new TrainerException("Trainer not found", HttpStatus.NOT_FOUND));
		if (!passwordEncoder.matches(trainerLoginRequest.getPassword(), trainer.getPassword())) {
			throw new TrainerException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		String otp = otpService.generateOtpAndStore(trainerLoginRequest.getEmail());
		otpService.sendOtpToEmail(trainerLoginRequest.getEmail(), otp);

		return "OTP sent to registered email";

	}

	@Override
	public ProfileResponse getProfile(Trainer trainer) {
		ProfileResponse result= new ProfileResponse();
		result.setUniqueId(trainer.getTrainerId());
		result.setName(trainer.getName());
		result.setEmail(trainer.getEmail());
		result.setMobile(trainer.getMobile());
		return result;
	}

	@Override
	@Transactional
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
	public Page<Trainer> viewAllTrainers(String stack, String name, String trainerId,String branchId, Pageable pageable) {
	    try {
	        return trainerRepository.findByFilters(stack, name, trainerId, branchId, pageable);
	    } catch (Exception e) {
	        throw new TrainerException("Failed to fetch trainers", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@Override
	public Trainer getByEmail(String email) {
		// TODO Auto-generated method stub
		return trainerRepository.findByEmail(email).orElse(null);
	}
	
	@Override
	public Trainer getById(String tainerId) {
		return trainerRepository.findById(tainerId).orElse(null);
	}


	@Override
	public Trainer disableTrainerById(String Id) {
		// TODO Auto-generated method stub
		Trainer trainer = trainerRepository.findById(Id)
	            .orElseThrow(() -> new TrainerException( "Trainer not found with ID: " + Id,   HttpStatus.NOT_FOUND));
		
		if (!trainer.isActive()) {
            throw new TrainerException( "Trainer is already suspended", HttpStatus.BAD_REQUEST);
        }
		
		trainer.setActive(false);
        trainer.setUpdatedAt(LocalDateTime.now());
        return trainerRepository.save(trainer);
	}


	@Override
	public Page<Trainer> getAllTrainers(String programHeadId, String q,String branchId, Pageable pageable) {
		// TODO Auto-generated method stub
		return trainerRepository.getAllTrainers(programHeadId,q,branchId, pageable);
	}


	@Override
	public Trainer findByPasswordResetToken(String token) {
		return trainerRepository.findByPasswordResetToken(token);
	}


	@Override
	public Trainer getTrainer(String programHead) {
		return trainerRepository.getTrainer(programHead);
	}
	
	
	@Override
	public Map getTrainerStats(String programHead) {
		
		Map<String, Long> stats = new LinkedHashMap<>();
		stats.put("totalTeamCount",trainerRepository.countTrainer(programHead, null));
		stats.put("active",trainerRepository.countTrainer(programHead, true));
		stats.put("inActive", trainerRepository.countTrainer(programHead, false));
		stats.put("fullTotal", trainerRepository.countTrainer(null, null));
		return  stats;
	}
	
	
	

}