package com.pentagon.app.restController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.StudentAdminException;
import com.pentagon.app.mapper.JobDescriptionMapper;
import com.pentagon.app.request.MangerJdStatusUpdateRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.serviceImpl.JobDescriptionServiceImp;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/studentAdmin/")
public class StudentAdminController {
	@Autowired
	private JobDescriptionService jobDescriptionService;
	@Autowired
	private TrainerService trainerService;
	
	@Autowired
	private JobDescriptionMapper jobDescriptionMapper;
	
	@GetMapping("/secure/jd")
	@PreAuthorize("hasRole('STUDNETADMIN')")
	public ResponseEntity<?> getAllJds(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String companyName, 
			@RequestParam(required = false) String stack,
			@RequestParam(required = false) String role,
			@RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false) String qualification, 
			@RequestParam(required = false) String stream,
			@RequestParam(required = false) Double percentage, 
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
		Page<JobDescriptionDTO> jobDescriptions = jobDescriptionService.findAllJobDescriptions(companyName, stack, role,
				isClosed, minYearOfPassing, maxYearOfPassing, qualification, stream, percentage, null,null, status,
				startDate, endDate, pageable).map(jobDescription -> jobDescriptionMapper .toDTO(jobDescription));
		return ResponseEntity.ok(new ApiResponse<>("success", "Jd results", jobDescriptions));
	}
	
	
	
	@GetMapping("/secure/viewAllTrainers")
	@PreAuthorize("hasRole('STUDNETADMIN')")
	public ResponseEntity<?> viewAllTrainers(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String stack, @RequestParam(required = false) String name,
			@RequestParam(required = false) String trainerId) {

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());

		Page<Trainer> trainers = trainerService.viewAllTrainers(stack, name, trainerId, pageable);

		Page<TrainerDTO> TrainerDTOPage = trainers.map(trainer -> {
			TrainerDTO dto = new TrainerDTO();
			dto.setTrainerId(trainer.getTrainerId());
			dto.setName(trainer.getName());
			dto.setEmail(trainer.getEmail());
			dto.setMobile(trainer.getMobile());
			dto.setQualification(trainer.getQualification());
			dto.setYearOfExperiences(trainer.getYearOfExperiences());
			dto.setActive(trainer.isAcitve());
			dto.setCreatedAt(trainer.getCreatedAt());
			dto.setUpdatedAt(trainer.getUpdatedAt());
			return dto;
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers fetched successfully", TrainerDTOPage));
	}
	
	

}
