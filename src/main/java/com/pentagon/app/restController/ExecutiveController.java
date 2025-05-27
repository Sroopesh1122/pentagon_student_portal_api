package com.pentagon.app.restController;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.request.AddJobDescriptionRequest;
import com.pentagon.app.request.UpdateClosuresRequest;
import com.pentagon.app.request.UpdateJobDescriptionRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.JobDescriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/executive")
public class ExecutiveController {
	
	@Autowired
	private ExecutiveService executiveService;
	
	@Autowired
	private ActivityLogService activityLogService;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
		
	@PostMapping("/secure/addJobDescription")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> addJobDescription(
			@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@Valid @RequestBody AddJobDescriptionRequest newJd,
			BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
		{
	            throw new ExecutiveException("Invalid input data", HttpStatus.BAD_REQUEST);   
		}
		if(executiveDetails.getExecutive()==null)
		{
			throw new ExecutiveException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		JobDescription jd=new JobDescription();
		jd.setJobDescriptionId(null);
        jd.setCompanyName(newJd.getCompanyName());
        jd.setWebsite(newJd.getWebsite());
        jd.setDescription(newJd.getDescription());
        jd.setRole(newJd.getRole());
        jd.setQualification(newJd.getQualification());
        jd.setStream(newJd.getStream());
        jd.setPercentage(newJd.getPercentage());
        jd.setMinYearOfPassing(newJd.getMinYearOfPassing());
        jd.setMaxYearOfPassing(newJd.getMaxYearOfPassing());
        jd.setStack(newJd.getStack());
        jd.setSalaryPackage(newJd.getSalaryPackage());
        jd.setNumberOfRegistrations(newJd.getNoOfRegistraions());
        jd.setMockRating(newJd.getMockRating());
        jd.setManagerApproval(false);
        jd.setCurrentRegistrations(0);
        executiveService.addJobDescription(jd);
		activityLogService.log(executiveDetails.getExecutive().getEmail(), 
				executiveDetails.getExecutive().getExecutiveId(), 
				"EXECUTIVE", 
				"Executive with ID " + executiveDetails.getExecutive().getExecutiveId() + " Added new job Description ");
		return ResponseEntity.ok(new ApiResponse<>("status","JobDescription added successfully",null));
	}
	
	@PostMapping("/secure/updateJobDescription")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> updateJobDescriptionBasedOnRegistrations(@AuthenticationPrincipal CustomUserDetails executiveDetails,
	        @Valid @RequestBody UpdateJobDescriptionRequest request, BindingResult bindingResult) {

		if(bindingResult.hasErrors()) {
			throw new ExecutiveException("Invaid Input Data", HttpStatus.BAD_REQUEST );
		}
		
		if(executiveDetails == null) {
			throw new ExecutiveException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}
		
	    JobDescription jobDescription = jobDescriptionRepository.findByJobDescriptionId(request.getJobDescriptionId())
	            .orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));

	    //both update and auto-closing
	    jobDescription.updateCurrentRegistrations(request.getCurrentRegistrations());

	    executiveService.updateJobDescription(jobDescription);

	    String message = jobDescription.isClosed() 
	            ? "Registrations updated and JD auto-closed" 
	            : "Registrations updated successfully";

	    return ResponseEntity.ok(new ApiResponse<>("success", message, null));
	}
	
	@PostMapping("/secure/updateClosures")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> updateClosures(@AuthenticationPrincipal CustomUserDetails executiveDetails,
	        @Valid @RequestBody UpdateClosuresRequest request, BindingResult bindingResult ) {
	    
		if(bindingResult.hasErrors()) {
			throw new ExecutiveException("Invaid Input Data", HttpStatus.BAD_REQUEST);
		}
		
		if(executiveDetails == null) {
			throw new ExecutiveException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}
		
	    JobDescription jobDescription = jobDescriptionRepository.findByJobDescriptionId(request.getJobDescriptionId())
	            .orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));

	    if (!jobDescription.isClosed()) {
	        throw new IllegalStateException("Job Description must be closed first");
	    }
	    
	    if (request.getNumberOfClosures() < 0 || 
	        request.getNumberOfClosures() > jobDescription.getCurrentRegistrations()) {
	        throw new IllegalArgumentException("Invalid closure count");
	    }

	    // Update closure data
	    jobDescription.setNumberOfClosures(request.getNumberOfClosures());
	    jobDescription.setJdStatus(request.getNumberOfClosures() >= 0); // true if any closures

	    executiveService.updateJobDescription(jobDescription);

	    return ResponseEntity.ok(new ApiResponse<>("success", "Closures updated successfully", null));
	}
	
	@GetMapping("/secure/{jobDescriptionId}")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> getJobDescriptionById(@PathVariable String jobDescriptionId){
		
		Optional<JobDescription> jobDescription = jobDescriptionRepository.findByJobDescriptionId(jobDescriptionId);
		
		if(jobDescription.isPresent()) {	
			return ResponseEntity.ok(new ApiResponse<>("success", "Job Description Fetched",jobDescription.get()));
		}
		else {
			throw new JobDescriptionException("Job Description Not Found", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/secure/viewAllJobDescriptions")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> viewAllJobDescriptions(@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int limit,
	        @RequestParam(required = false) String companyName,
	        @RequestParam(required = false ,defaultValue = "") String stack,
	        @RequestParam(required = false) String role,
	        @RequestParam(required = false) Boolean isClosed,
	        @RequestParam(required = false) Integer minYearOfPassing,
	        @RequestParam(required = false) Integer maxYearOfPassing,
	        @RequestParam(required = false,defaultValue = "") String qualification,
	        @RequestParam(required = false,defaultValue = "") String stream,
	        @RequestParam(required = false) Double percentage){
		
		if(executiveDetails == null) {
			throw new ExecutiveException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("created_at").descending());
		
		System.out.println(stream);
		
		Page<JobDescription> jobDescriptions = executiveService.findAllJobDescriptions( companyName, stack, role, isClosed,
									minYearOfPassing, maxYearOfPassing,qualification, stream, percentage, pageable );
		
		
		Page<JobDescriptionDTO> JobDescriptionDTOResponse = jobDescriptions.map(jobDescription -> {
			JobDescriptionDTO jobDescriptionDTO = new JobDescriptionDTO();
			jobDescriptionDTO.setId(jobDescription.getId());
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
			jobDescriptionDTO.setJdStatus(jobDescription.isJdStatus());
			jobDescriptionDTO.setManagerApproval(jobDescription.isManagerApproval());
			jobDescriptionDTO.setNumberOfClosures(jobDescription.getNumberOfClosures());
			jobDescriptionDTO.setClosed(jobDescription.isClosed());
			jobDescriptionDTO.setCreatedAt(jobDescription.getCreatedAt());
			jobDescriptionDTO.setUpdatedAt(jobDescription.getUpdatedAt());
	        return jobDescriptionDTO;	
		});
		
		
		return ResponseEntity.ok(
	            new ApiResponse<>("success", "Job Descriptions fetched successfully", JobDescriptionDTOResponse)
	        );
		//min yr of passing, max yr of passing , stream, qualification , stream, percentage
	}
	
	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> getExecutiveProfile(@AuthenticationPrincipal CustomUserDetails executiveDetails) {
		if(executiveDetails.getExecutive() == null) {
			throw new ExecutiveException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
	    Executive  executive = executiveDetails.getExecutive();
	    ProfileResponse details = executiveService.getProfile(executive);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Executive Profile", details));
	}
	
	
}
