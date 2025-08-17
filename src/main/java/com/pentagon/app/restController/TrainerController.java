package com.pentagon.app.restController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.ProgramHeadException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.mapper.TrainerMapper;
import com.pentagon.app.request.UpdateTrainerTechnology;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.BatchTechTrainerService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ProgramHeadService;
import com.pentagon.app.service.TechnologyService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlTemplates;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/trainer")
public class TrainerController {

	@Autowired
	private TrainerService trainerService;

	@Autowired
	private ActivityLogService activityLogService;

	@Autowired
	private TechnologyService technologyService;

	@Autowired
	private BatchTechTrainerService batchTechTrainerService;

	@Autowired
	private TrainerMapper trainerMapper;

	@Autowired
	private CloudinaryServiceImp cloudinaryService;

	@Autowired
	private HtmlTemplates htmlTemplates;

	@Autowired
	private MailService mailService;

	@PutMapping("/secure/update")
	@PreAuthorize("hasAnyRole('TRAINER','PROGRAMHEAD')")
	public ResponseEntity<?> updateTrainer(@AuthenticationPrincipal CustomUserDetails trainerDetails,
			@RequestParam(required = false) String name, @RequestParam(required = false) String mobile,
			@RequestParam(required = false) String qualification,
			@RequestParam(required = false) Integer yearOfExperiences, @RequestParam(required = false) String gender,
			@RequestParam(required = false) String bio, @RequestPart(required = false) MultipartFile profileFile,
			@RequestParam(required = false) LocalDate dob) {

		if (trainerDetails.getTrainer() == null && trainerDetails.getProgramHead() == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}

		Trainer trainer = null;

		if (trainerDetails.getTrainer() != null) {
			trainer = trainerDetails.getTrainer();
		} else {
			trainer = trainerService.getTrainer(trainerDetails.getProgramHead().getId());
		}

		if (trainer == null) {
			throw new TrainerException("Trainer not found", HttpStatus.NOT_FOUND);
		}

		if (name != null) {
			trainer.setName(name);
		}
		if (mobile != null) {
			trainer.setMobile(mobile);
		}
		if (qualification != null) {
			trainer.setQualification(qualification);
		}
		if (yearOfExperiences != null) {
			trainer.setYearOfExperiences(yearOfExperiences);
		}
		if (gender != null) {
			trainer.setGender(gender);
		}
		if (bio != null) {
			trainer.setBio(bio);
		}
		if (dob != null) {
			trainer.setDob(dob);
		}

		if (profileFile != null) {
			if (trainer.getProfilePublicId() != null) {
				cloudinaryService.deleteFile(trainer.getProfilePublicId(), "image");
			}
			Map<String, Object> uploadResponse = cloudinaryService.uploadImage(profileFile);
			trainer.setProfilePublicId(uploadResponse.get("public_id").toString());
			trainer.setProfileImgUrl(uploadResponse.get("secure_url").toString());

		}

		Trainer updatedTrainer = trainerService.updateTrainer(trainer);
		return ResponseEntity.ok(new ApiResponse<>("success", "Profile Updated Successfully", null));

	}

	@PutMapping("/secure/update/technologies")
	@PreAuthorize("hasAnyRole('PROGRAMHEAD','ADMIN')")
	public ResponseEntity<?> updateTrainerTechnologies(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody UpdateTrainerTechnology request) {

		if (userDetails.getProgramHead() == null && userDetails.getAdmin() == null) {
			throw new ProgramHeadException("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

		boolean isAdmin = false;

		if (userDetails.getAdmin() != null) {
			isAdmin = true;
		}

		Trainer trainer = trainerService.getById(request.getTrainerId());
		if (trainer == null) {
			throw new ProgramHeadException("Trainer Not Found", HttpStatus.NOT_FOUND);
		}

		if (!isAdmin) {
			if (!trainer.getProgramHeadId().equals(userDetails.getProgramHead().getId())) {
				throw new ProgramHeadException("Your are not authorized to change the technologies",
						HttpStatus.BAD_REQUEST);
			}
		}
		trainer.getTechnologies().clear();

		Arrays.asList(request.getTechId().split(",")).forEach(techId -> {

			Technology technology = technologyService.getTechnologyById(techId.trim()).orElse(null);
			if (technology == null) {
				throw new ProgramHeadException("Technology Not Found", HttpStatus.BAD_REQUEST);
			}

			trainer.getTechnologies().add(technology);
		});

		trainerService.updateTrainer(trainer);

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Technologies updated Successfully", null));

	}

	@PutMapping("/secure/block/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','PROGRAMHEAD')")
	public ResponseEntity<?> blockTrainer(@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable String id) {
		if (userDetails.getAdmin() == null && userDetails.getProgramHead() == null) {
			throw new TrainerException("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

		Trainer trainer = trainerService.getById(id);

		if (trainer == null) {
			throw new TrainerException("Trainer Not Found", HttpStatus.NOT_FOUND);
		}

		if (userDetails.getProgramHead() != null) {
			if (!trainer.getProgramHeadId().equals(userDetails.getProgramHead().getId())) {
				throw new TrainerException("Your Are not authorized to block this account", HttpStatus.BAD_REQUEST);
			}
		}

		trainer.setActive(false);

		trainerService.updateTrainer(trainer);

		trainerService.updateTrainer(trainer);

		String mailMessage = htmlTemplates.getAccountBlockedEmail(trainer.getName());

		mailService.sendAsync(trainer.getEmail(), "Account Blocked - Pentagon Space", mailMessage);
		return ResponseEntity.ok(new ApiResponse<>("success", "Account Blocked Successfully", null));

	}

	@PutMapping("/secure/unblock/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','PROGRAMHEAD')")
	public ResponseEntity<?> unBlockTrainer(@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable String id) {
		if (userDetails.getAdmin() == null && userDetails.getProgramHead() == null) {
			throw new TrainerException("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

		Trainer trainer = trainerService.getById(id);

		if (trainer == null) {
			throw new TrainerException("Trainer Not Found", HttpStatus.NOT_FOUND);
		}

		if (userDetails.getProgramHead() != null) {
			if (!trainer.getProgramHeadId().equals(userDetails.getProgramHead().getId())) {
				throw new TrainerException("Your Are not authorized to unblock this account", HttpStatus.BAD_REQUEST);
			}
		}

		trainer.setActive(true);

		trainerService.updateTrainer(trainer);

		String mailMessage = htmlTemplates.getAccountUnblockedEmail(trainer.getName());

		mailService.sendAsync(trainer.getEmail(), "Account Unblocked - Pentagon Space", mailMessage);

		return ResponseEntity.ok(new ApiResponse<>("success", "Account UnBlocked Successfully", null));

	}

	@Transactional
	@GetMapping("/secure/profile")
	@PreAuthorize("hasAnyRole('TRAINER','PROGRAMHEAD')")
	public ResponseEntity<?> getAdminProfile(@AuthenticationPrincipal CustomUserDetails trainerDetails) {
		if (trainerDetails.getTrainer() == null && trainerDetails.getProgramHead() == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}

		Trainer trainer = null;

		if (trainerDetails.getTrainer() != null) {
			trainer = trainerDetails.getTrainer();
		} else {
			trainer = trainerService.getTrainer(trainerDetails.getProgramHead().getId());
		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Profile", trainer));
	}

	@GetMapping("/public/trainer/{id}/schedule")
	@PreAuthorize("hasAnyRole('TRAINER','PROGRAMHEAD')")
	public ResponseEntity<?> getTrainerScheduleInfo(@AuthenticationPrincipal CustomUserDetails trainerDetails,
			@PathVariable String id) {

		Trainer findTrainer = trainerService.getById(id);

		if (findTrainer == null) {
			throw new TrainerException("Trainer Not Found", HttpStatus.NOT_FOUND);
		}

		List<BatchTechTrainer> trainerScheduleInfo = batchTechTrainerService.getTrainerSchedule(id);

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Profile", trainerScheduleInfo));
	}

	@GetMapping("/secure/all")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','ADMIN','PROGRAMHEAD')")
	public ResponseEntity<?> getAllTrainers(
			@AuthenticationPrincipal CustomUserDetails programHeadDetails,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String q,
			@RequestParam(required = false) String branchId,
			@RequestParam(required = false) String programHeadId) {

		Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
		
		// if we pass null then all trainers are fetched under every program Head
		Page<TrainerDTO> trainers = trainerService.getAllTrainers(programHeadId, q,branchId, pageable)
				.map(trainer -> trainerMapper.toDTO(trainer));

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers data", trainers));
	}

	// individual trainers
	@Transactional
	@GetMapping("/secure/{id}")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','ADMIN','PROGRAMHEAD')")
	public ResponseEntity<?> getTrainerById(@PathVariable String id) {

		Trainer findTrainer = trainerService.getById(id);

		if (findTrainer == null) {
			throw new TrainerException("Trainer not found", HttpStatus.NOT_FOUND);
		}

		TrainerDTO trainerDTO = trainerMapper.toDTO(findTrainer);

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer data", trainerDTO));
	}

}