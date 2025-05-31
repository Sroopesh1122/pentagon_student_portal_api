package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.repository.TrainerRepository;
import com.pentagon.app.request.AddTrainerRequest;
import com.pentagon.app.request.TrainerLoginRequest;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.utils.HtmlContent;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.utils.PasswordGenration;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class TrainerServiceImpl implements TrainerService {

	@Autowired
	private TrainerRepository trainerRepository;

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
	public Trainer updateTrainer(Trainer trainer) {
		try {
			trainer.setUpdatedAt(LocalDateTime.now());
			return trainerRepository.save(trainer);

		} catch (Exception e) {
			throw new TrainerException("Failed to update Trainer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public String loginWithPassword(TrainerLoginRequest trainerLoginRequest) {
		Trainer trainer = trainerRepository.findByEmail(trainerLoginRequest.getEmail())
				.orElseThrow(() -> new TrainerException("Trainer not found", HttpStatus.NOT_FOUND));
		if (!passwordEncoder.matches(trainerLoginRequest.getPassword(), trainer.getPassword())) {
			throw new TrainerException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		String otp = otpService.generateOtpAndStore(trainerLoginRequest.getEmail());
		otpService.sendOtpToEmail(trainerLoginRequest.getEmail(), otp);

		return "OTP sent to registered email";

	}

	@Override
	public ProfileResponse getProfile(Trainer trainer) {
		ProfileResponse result = new ProfileResponse();
		result.setUniqueId(trainer.getTrainerId());
		result.setName(trainer.getName());
		result.setEmail(trainer.getEmail());
		result.setMobile(trainer.getMobile());
		return result;
	}

	@Override
	@Transactional
	public Trainer addTrainer(Trainer trainer) {
		try {
			trainer.setCreatedAt(LocalDateTime.now());
			return trainerRepository.save(trainer);

		} catch (Exception e) {
			throw new TrainerException("Failed to Add Trainer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@Override
	public void addTrainer(CustomUserDetails managerDetails, @Valid AddTrainerRequest newTrainerRequest) {
		try {
			if (!isEmailAvailable(newTrainerRequest.getEmail())) {
				throw new TrainerException("Email already exists", HttpStatus.CONFLICT);
			}
			Trainer trainer = new Trainer();
			trainer.setTrainerId(idGeneration.generateId("TRAINER"));
			trainer.setName(newTrainerRequest.getName());
			trainer.setEmail(newTrainerRequest.getEmail());
			trainer.setMobile(newTrainerRequest.getMobile());
			trainer.setTrainerStack(newTrainerRequest.getTrainerStack());
			trainer.setTechnologies(newTrainerRequest.getTechnologies());
			trainer.setYearOfExperiences(newTrainerRequest.getYearOfExperiences());
			trainer.setQualification(newTrainerRequest.getQualification());
			trainer.setAcitve(true);
			
			String password = passwordGenration.generateRandomPassword();
			try {
				trainer.setPassword(passwordEncoder.encode(password));
			} catch (Exception e) {
				throw new TrainerException("Password encoding failed", HttpStatus.INTERNAL_SERVER_ERROR);
			}

			trainer.setCreatedAt(LocalDateTime.now());
			trainer= trainerRepository.save(trainer);

			String htmlContent = htmlContentService.getHtmlContent(trainer.getName(), trainer.getEmail(), password);

			activityLogService.log(managerDetails.getManager().getEmail(), managerDetails.getManager().getManagerId(),
					"MANAGER", "Manager with ID " + managerDetails.getManager().getManagerId()
							+ " added a new Trainer with ID " + trainer.getTrainerId());
			
			try {
				mailService.sendPasswordEmail(trainer.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
						htmlContent);
			} catch (Exception e) {
				throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
	        throw new TrainerException("Failed to create Trainer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@Override
	public Page<Trainer> viewAllTrainers(String stack, String name, String trainerId, Pageable pageable) {
		try {
			return trainerRepository.findByFilters(stack, name, trainerId, pageable);
		} catch (Exception e) {
			throw new TrainerException("Failed to fetch trainers", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public boolean checkExistsByEmail(String email) {
		// TODO Auto-generated method stub
		return trainerRepository.existsByEmail(email);
	}

	public boolean isEmailAvailable(String email) {
		return trainerRepository.findByEmail(email).isEmpty();
	}

}