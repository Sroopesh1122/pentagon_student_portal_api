package com.pentagon.app.restController;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.entity.JdRoundHistory;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.request.ScheduleRoundRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.JdStatusRoundHistoryService;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.ManagerService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/jd")
public class JobDescriptionController {
	
	
	@Autowired
	private JobDescriptionService jobDescriptionService;
	
	
	@Autowired
	private ManagerService managerService;
	
	
	@Autowired
	private JdStatusRoundHistoryService jdStatusRoundHistoryService;
	
	@GetMapping("/secure/{jobDescriptionId}")
	@PreAuthorize("hasAnyRole('TRAINER','EXECUTIVE','MANAGER')")
	public ResponseEntity<?> getJobDescriptionById(@PathVariable String jobDescriptionId) {

		Optional<JobDescription> jobDescriptionOtp = jobDescriptionService.findByJobDescriptionId(jobDescriptionId);

		if (jobDescriptionOtp.isEmpty()) {
			throw new JobDescriptionException("Job Description Not Found", HttpStatus.NOT_FOUND);
		}

		JobDescription jobDescription = jobDescriptionOtp.get();
		JobDescriptionDTO jobDescriptionDTO = new JobDescriptionDTO();
		jobDescriptionDTO.setCompanyLogo(jobDescription.getCompanyLogo());
		jobDescriptionDTO.setJobDescriptionId(jobDescription.getJobDescriptionId());
		jobDescriptionDTO.setCompanyName(jobDescription.getCompanyName());
		jobDescriptionDTO.setWebsite(jobDescription.getWebsite());
		jobDescriptionDTO.setRole(jobDescription.getRole());
		jobDescriptionDTO.setStack(jobDescription.getStack());
		jobDescriptionDTO.setQualification(jobDescription.getQualification());
		jobDescriptionDTO.setStream(jobDescription.getStream());
		jobDescriptionDTO.setPercentage(jobDescription.getPercentage());
		jobDescriptionDTO.setMinYearOfPassing(jobDescription.getMinYearOfPassing());
		jobDescriptionDTO.setMaxYearOfPassing(jobDescription.getMaxYearOfPassing());
		jobDescriptionDTO.setSalaryPackage(jobDescription.getSalaryPackage());
		jobDescriptionDTO.setNumberOfRegistrations(jobDescription.getNumberOfRegistrations());
		jobDescriptionDTO.setCurrentRegistrations(jobDescription.getCurrentRegistrations());
		jobDescriptionDTO.setMockRating(jobDescription.getMockRating());
		jobDescriptionDTO.setJdStatus(jobDescription.getJdStatus());
		jobDescriptionDTO.setManagerApproval(jobDescription.isManagerApproval());
		jobDescriptionDTO.setNumberOfClosures(jobDescription.getNumberOfClosures());
		jobDescriptionDTO.setClosed(jobDescription.isClosed());
		jobDescriptionDTO.setCreatedAt(jobDescription.getCreatedAt());
		jobDescriptionDTO.setUpdatedAt(jobDescription.getUpdatedAt());
		jobDescriptionDTO.setLocation(jobDescription.getLocation());
		jobDescriptionDTO.setExecutive(jobDescription.getExecutive());
		jobDescriptionDTO.setPostedBy(jobDescription.getPostedBy());
		jobDescriptionDTO.setDescription(jobDescription.getDescription());
		jobDescriptionDTO.setSkills(jobDescription.getSkills());
		jobDescriptionDTO.setJdActionReason(jobDescription.getJdActionReason());
		jobDescriptionDTO.setAcardemicGap(jobDescription.getAcardemicGap());
		jobDescriptionDTO.setBacklogs(jobDescription.getBacklogs());
		jobDescriptionDTO.setBondDetails(jobDescription.getBondDetails());
		jobDescriptionDTO.setSalaryDetails(jobDescription.getSalaryDetails());
		jobDescriptionDTO.setStautsHistory(jobDescription.getStautsHistory());
		jobDescriptionDTO.setRoundHistory(jobDescription.getRoundHistory());
		jobDescriptionDTO.setAboutCompany(jobDescription.getAboutCompany());
		jobDescriptionDTO.setInterviewDate(jobDescription.getInterviewDate());
		jobDescriptionDTO.setGenderPreference(jobDescription.getGenderPreference());
		jobDescriptionDTO.setRolesAndResponsibility(jobDescription.getRolesAndResponsibility());
		jobDescriptionDTO.setGeneric(jobDescription.getGeneric());

		Manager manager = managerService.getManagerById(jobDescription.getManagerId());
		if (manager != null) {
			jobDescriptionDTO.setManagerId(jobDescription.getManagerId());
			jobDescriptionDTO.setManagerName(manager.getName());
		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Job Description Fetched", jobDescriptionDTO));

	}

	@GetMapping("/secure/all")
	@PreAuthorize("hasAnyRole('TRAINER','MANAGER')")
	public ResponseEntity<?> viewAllJobDescriptions(
			@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String companyName,
			@RequestParam(required = false, defaultValue = "") String stack,
			@RequestParam(required = false) String role, 
			@RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false, defaultValue = "") String qualification,
			@RequestParam(required = false, defaultValue = "") String stream,
			@RequestParam(required = false) Double percentage,
			@RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {

		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
		
		System.out.println(percentage);

		Page<JobDescription> jobDescriptions = jobDescriptionService.findAllJobDescriptions(companyName, stack, role,
				isClosed, minYearOfPassing, maxYearOfPassing, qualification, stream, percentage, null, null,
				status, startDate, endDate, pageable);

		Page<JobDescriptionDTO> JobDescriptionDTOResponse = jobDescriptions.map(jobDescription -> {
			JobDescriptionDTO jobDescriptionDTO = new JobDescriptionDTO();
			jobDescriptionDTO.setCompanyLogo(jobDescription.getCompanyLogo());
			jobDescriptionDTO.setJobDescriptionId(jobDescription.getJobDescriptionId());
			jobDescriptionDTO.setCompanyName(jobDescription.getCompanyName());
			jobDescriptionDTO.setWebsite(jobDescription.getWebsite());
			jobDescriptionDTO.setRole(jobDescription.getRole());
			jobDescriptionDTO.setStack(jobDescription.getStack());
			jobDescriptionDTO.setQualification(jobDescription.getQualification());
			jobDescriptionDTO.setStream(jobDescription.getStream());
			jobDescriptionDTO.setPercentage(jobDescription.getPercentage());
			jobDescriptionDTO.setMinYearOfPassing(jobDescription.getMinYearOfPassing());
			jobDescriptionDTO.setMaxYearOfPassing(jobDescription.getMaxYearOfPassing());
			jobDescriptionDTO.setSalaryPackage(jobDescription.getSalaryPackage());
			jobDescriptionDTO.setNumberOfRegistrations(jobDescription.getNumberOfRegistrations());
			jobDescriptionDTO.setCurrentRegistrations(jobDescription.getCurrentRegistrations());
			jobDescriptionDTO.setMockRating(jobDescription.getMockRating());
			jobDescriptionDTO.setJdStatus(jobDescription.getJdStatus());
			jobDescriptionDTO.setManagerApproval(jobDescription.isManagerApproval());
			jobDescriptionDTO.setNumberOfClosures(jobDescription.getNumberOfClosures());
			jobDescriptionDTO.setClosed(jobDescription.isClosed());
			jobDescriptionDTO.setCreatedAt(jobDescription.getCreatedAt());
			jobDescriptionDTO.setUpdatedAt(jobDescription.getUpdatedAt());
			jobDescriptionDTO.setLocation(jobDescription.getLocation());
			jobDescriptionDTO.setSkills(jobDescription.getSkills());
			jobDescriptionDTO.setPostedBy(jobDescription.getPostedBy());
			jobDescriptionDTO.setJdStatus(jobDescription.getJdStatus());
			jobDescriptionDTO.setJdActionReason(jobDescription.getJdActionReason());
			jobDescriptionDTO.setAcardemicGap(jobDescription.getAcardemicGap());
			jobDescriptionDTO.setBacklogs(jobDescription.getBacklogs());
			jobDescriptionDTO.setBondDetails(jobDescription.getBondDetails());
			jobDescriptionDTO.setSalaryDetails(jobDescription.getSalaryDetails());
			jobDescriptionDTO.setStautsHistory(jobDescription.getStautsHistory());
			jobDescriptionDTO.setRoundHistory(jobDescription.getRoundHistory());
			jobDescriptionDTO.setAboutCompany(jobDescription.getAboutCompany());
			jobDescriptionDTO.setInterviewDate(jobDescription.getInterviewDate());
			jobDescriptionDTO.setGenderPreference(jobDescription.getGenderPreference());
			jobDescriptionDTO.setRolesAndResponsibility(jobDescription.getRolesAndResponsibility());

			Manager jdManager = managerService.getManagerById(jobDescription.getManagerId());
			if (jdManager != null) {
				jobDescriptionDTO.setManagerId(jdManager.getManagerId());
				jobDescriptionDTO.setManagerName(jdManager.getName());
			}

			return jobDescriptionDTO;
		});

		return ResponseEntity
				.ok(new ApiResponse<>("success", "Job Descriptions fetched successfully", JobDescriptionDTOResponse));
	}


	@Transactional
	@PostMapping("/secure/schedule-round")
	public ResponseEntity<?> scheduleNewRound(@RequestBody @Valid ScheduleRoundRequest request,BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
		{
			throw new JobDescriptionException("Invalid Data", HttpStatus.BAD_REQUEST);
		}
		
		JobDescription jobDescription = jobDescriptionService.finById(request.getJdId());
		
		if(jobDescription==null)
		{
			throw new JobDescriptionException("Jd not found", HttpStatus.NOT_FOUND);
		}
		
		
		JdRoundHistory jdRoundHistory =  jdStatusRoundHistoryService.findRound(request.getRoundName(), request.getJdId());
		
		if(jdRoundHistory!=null)
		{
			throw new JobDescriptionException("Round Already exists", HttpStatus.CONFLICT);
		}
		
		//Adding New ROund for the JD
		JdRoundHistory createRound = new JdRoundHistory();
		createRound.setJobDescription(jobDescription);
		createRound.setRound(request.getRoundName());
		String isoDate = request.getDate(); // "2025-07-01T06:39:03.000Z"
		if (isoDate.endsWith("Z")) {
		    isoDate = isoDate.substring(0, isoDate.length() - 1);
		}
		if (isoDate.contains(".")) {
		    isoDate = isoDate.substring(0, isoDate.indexOf("."));
		}
		createRound.setScheduleDate(LocalDateTime.parse(isoDate));
		
		//updating current Round for JD
		jobDescription.setCurrentRound(request.getRoundName());
		jobDescriptionService.updateJobDescription(jobDescription);
		
		jdStatusRoundHistoryService.addRound(createRound);
		
		
		return ResponseEntity.ok( new ApiResponse<>("success","New Round Created and Jd Updated", null));
		
		
		
		
		
		
	}
	
}
