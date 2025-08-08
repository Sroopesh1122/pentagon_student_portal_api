package com.pentagon.app.restController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.Dto.ProgramHeadDTO;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.ProgramHead;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.exception.ProgramHeadException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.mapper.JobDescriptionMapper;
import com.pentagon.app.mapper.ProgramHeadMapper;
import com.pentagon.app.mapper.TrainerMapper;
import com.pentagon.app.request.AddTrainerRequest;
import com.pentagon.app.request.UpdateTrainerTechnology;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.ProgramHeadService;
import com.pentagon.app.service.TechnologyService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlTemplates;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.utils.PasswordGenration;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/programHead")
public class ProgramHeadController {

	@Autowired
	private TrainerService trainerService;

	@Autowired
	private IdGeneration idGeneration;

	@Autowired
	private PasswordGenration passwordGenration;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private HtmlTemplates htmlContentService;

	@Autowired
	private MailService mailService;

	@Autowired
	private ActivityLogService activityLogService;

	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private TrainerMapper trainerMapper;

	@Autowired
	private TechnologyService technologyService;

	@Autowired
	private ProgramHeadService programHeadService;

	@Autowired
	private CloudinaryServiceImp cloudinaryService;

	@Autowired
	private JobDescriptionMapper jobDescriptionMapper;

	@Autowired
	private ProgramHeadMapper programHeadMapper;

	@GetMapping("/secure/trainer-stats")
	@PreAuthorize("hasRole('PROGRAMHEAD')")
	public ResponseEntity<?> getProgramHeadStats(@AuthenticationPrincipal CustomUserDetails userDetails) {
		ProgramHead programHead = userDetails.getProgramHead();

		Map stats = trainerService.getTrainerStats(programHead.getId());

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Stats", stats));

	}

	@PostMapping("/secure/addTrainer")
	@PreAuthorize("hasRole('PROGRAMHEAD')")
	public ResponseEntity<?> addTrainer(@AuthenticationPrincipal CustomUserDetails programHeadDetails,
			@Valid @RequestBody AddTrainerRequest request, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ProgramHeadException("Invalid Input Data", HttpStatus.BAD_REQUEST);
		}

		Trainer findTrainer = trainerService.getByEmail(request.getEmail());

		if (findTrainer != null) {
			throw new ProgramHeadException("Email Already Exists", HttpStatus.CONFLICT);
		}

		Trainer trainer = new Trainer();
		trainer.setTrainerId(idGeneration.generateId("TRAINER"));
		trainer.setName(request.getName());
		trainer.setEmail(request.getEmail());
		trainer.setMobile(request.getMobile());
		trainer.setProgramHeadId(programHeadDetails.getProgramHead().getId());
		trainer.setCreatedAt(LocalDateTime.now());
		String password = passwordGenration.generateRandomPassword();
		trainer.setPassword(passwordEncoder.encode(password));

		List<Technology> trainerTechnologies = new ArrayList<>();

		request.getTechId().forEach(technologyId -> {
			Technology findTechnology = technologyService.getTechnologyById(technologyId).orElse(null);
			if (findTechnology == null) {
				throw new ProgramHeadException("No Technology found", HttpStatus.NOT_FOUND);
			}
			trainerTechnologies.add(findTechnology);
		});

		trainer.setTechnologies(trainerTechnologies);

		Trainer newTrainer = trainerService.addTrainer(trainer);

		String htmlContent = htmlContentService.getHtmlContent(trainer.getName(), trainer.getEmail(), password);
		try {
			mailService.send(trainer.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed", htmlContent);
		} catch (Exception e) {
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}


		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Added Successfully", null));
	}

	@GetMapping("/secure/jd")
	@PreAuthorize("hasRole('PROGRAMHEAD')")
	public ResponseEntity<?> getAllJds(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(required = false) String companyName,
			@RequestParam(required = false, defaultValue = "") String stack,
			@RequestParam(required = false) String role, @RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false, defaultValue = "") String qualification,
			@RequestParam(required = false, defaultValue = "") String stream,
			@RequestParam(required = false) Double percentage, @RequestParam(required = false) String status,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate)

	{

		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());

		// Will get only approved JD
		Page<JobDescription> jobDescriptions = jobDescriptionService.findAllJobDescriptions(companyName, stack, role,
				isClosed, minYearOfPassing, maxYearOfPassing, qualification, stream, percentage, null, null, "approved",
				startDate, endDate, pageable);

		Page<JobDescriptionDTO> JobDescriptionDTOResponse = jobDescriptions.map(jobDescription -> {
			return jobDescriptionMapper.toDTO(jobDescription);
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Job Descriptions Fetched", JobDescriptionDTOResponse));
	}

	@GetMapping("/secure/jd/{id}")
	@PreAuthorize("hasRole('PROGRAMHEAD')")
	public ResponseEntity<?> getJdById(@PathVariable String id) {
		Optional<JobDescription> jobDescriptionoptional = jobDescriptionService.findByJobDescriptionId(id);

		if (jobDescriptionoptional.isEmpty()) {
			throw new ProgramHeadException("Not Jd Found", HttpStatus.NOT_FOUND);
		}

		JobDescriptionDTO jobDescriptionDTO = jobDescriptionMapper.toDTO(jobDescriptionoptional.get());

		return ResponseEntity.ok(new ApiResponse<>("success", "JD Data", jobDescriptionDTO));

	}

	@PatchMapping("/secure/trainer/{id}/suspend")
	@PreAuthorize("hasRole('PROGRAMHEAD')")
	public ResponseEntity<?> suspendTrainer(@AuthenticationPrincipal CustomUserDetails programHeadDetails,
			@PathVariable String id) {

		Trainer suspendedTrainer = trainerService.disableTrainerById(id);


		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer suspended successfully", null));
	}

	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('PROGRAMHEAD')")
	public ResponseEntity<?> getProgramHeadProfile(@AuthenticationPrincipal CustomUserDetails programHeadDetails) {

		if (programHeadDetails.getProgramHead() == null) {
			throw new ProgramHeadException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}

		ProgramHead programHead = programHeadDetails.getProgramHead();

		Trainer trainer = trainerService.getTrainer(programHead.getId());

		return ResponseEntity.ok(new ApiResponse<>("success", "ProgramHead Profile", trainer));
	}

	@GetMapping("/secure/{id}")
	@PreAuthorize("hasAnyRole('PROGRAMHEAD','ADMIN')")
	public ResponseEntity<?> getProgramHead(@PathVariable String id) {

		ProgramHead programHead = programHeadService.getById(id);

		if (programHead == null) {
			throw new ProgramHeadException("Account Not Found", HttpStatus.NOT_FOUND);
		}

		ProgramHeadDTO programHeadDTO = programHeadMapper.toDTO(programHead);

		Trainer trainer = trainerService.getTrainer(programHead.getId());
		programHeadDTO.setTrainer(trainer);
		programHeadDTO.setProfileImgUrl(trainer.getProfileImgUrl());

		return ResponseEntity.ok(new ApiResponse<>("success", "ProgramHead Profile",programHeadDTO));
	}

	@PutMapping("/secure/block/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> blockAccount(@PathVariable String id) {

		ProgramHead programHead = programHeadService.getById(id);
		Trainer trainer = trainerService.getTrainer(id);

		if (programHead == null || trainer ==null) {
			throw new ProgramHeadException("Account Not Found", HttpStatus.NOT_FOUND);
		}

		programHead.setAcitve(false);
		trainer.setActive(false);
		trainerService.updateTrainer(trainer);
		programHeadService.update(programHead);
		

		return ResponseEntity.ok(new ApiResponse<>("success", "Account Blocked Successfully", null));
	}
	
	@PutMapping("/secure/unblock/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> unBlockAccount(@PathVariable String id) {

		ProgramHead programHead = programHeadService.getById(id);
		Trainer trainer = trainerService.getTrainer(id);
		

		if (programHead == null || trainer ==null) {
			throw new ProgramHeadException("Account Not Found", HttpStatus.NOT_FOUND);
		}

		programHead.setAcitve(true);
		trainer.setActive(true);
		trainerService.updateTrainer(trainer);
		programHeadService.update(programHead);

		return ResponseEntity.ok(new ApiResponse<>("success", "Account Un-Blocked Successfully", null));
	}

	@PutMapping("/secure/update")
	@PreAuthorize("hasRole('PROGRAMHEAD')")
	public ResponseEntity<?> updateTrainer(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestParam(required = false) String name, @RequestParam(required = false) String mobile,
			@RequestParam(required = false) String qualification,
			@RequestParam(required = false) Integer yearOfExperiences, @RequestParam(required = false) String gender,
			@RequestParam(required = false) String bio, @RequestPart(required = false) MultipartFile profileFile,
			@RequestParam(required = false) LocalDate dob) {

		if (userDetails.getProgramHead() == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}

		ProgramHead programHead = userDetails.getProgramHead();

		Trainer trainer = trainerService.getTrainer(programHead.getId());

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

}
