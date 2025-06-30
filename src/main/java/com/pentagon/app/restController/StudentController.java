package com.pentagon.app.restController;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.Dto.StudentDTO;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Student;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.mapper.StudentMapper;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;

@RestController
@RequestMapping("/api/student")
public class StudentController {


	@Autowired
	private StudentService studentService;
	
	@Autowired
	private CloudinaryServiceImp cloudinaryService;
	
	@Autowired
	private JobDescriptionService jobDescriptionService;
	
	@Autowired
	private StudentMapper studentMapper;
	
	
	@GetMapping("/secure/profile")
	public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails)
	{
		Student student = customUserDetails.getStudent();
		
		StudentDTO studentDTO =  studentMapper.toDto(student);
		
		return ResponseEntity.ok(new ApiResponse<>("success","Student Profile data",studentDTO));
			
	}
	
	
	
	@PutMapping("/secure/update")
	public ResponseEntity<?> updateProfile(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@RequestParam(required = false) String name,
		    @RequestParam(required = false) String gender,
		    @RequestParam(required = false) String mobile,
		    @RequestParam(required = false) String whatsappNo,
		    @RequestParam(required = false) String dob,
		    @RequestParam(required = false) String summary,
		    @RequestParam(required = false) String address,
		    @RequestParam(required = false) String state,
		    @RequestParam(required = false) String city,
		    @RequestParam(required = false) String experience,
		    @RequestParam(required = false) String skills,
		    @RequestParam(required = false) String tenthSchool,
		    @RequestParam(required = false) Integer tenthPassOutYear,
		    @RequestParam(required = false) Double tenthPercentage,
		    @RequestParam(required = false) String twelveSchool,
		    @RequestParam(required = false) Integer twelvePassOutYear,
		    @RequestParam(required = false) Double twelvePercentage,
		    @RequestParam(required = false) String gradSchool,
		    @RequestParam(required = false) String gradCourse,
		    @RequestParam(required = false) String gradBranch,
		    @RequestParam(required = false) Double gradPercentage,
		    @RequestParam(required = false) Double gradCgpa ,
		    @RequestParam(required =  false) Integer gradPassOutYear,
		    @RequestPart(required = false) MultipartFile profileImg)
	{
		Student student = customUserDetails.getStudent();
		
		if(profileImg!=null)
		{
			if(student.getProfilePublicId()!=null)
			{
				cloudinaryService.deleteFile(student.getProfilePublicId());
			}
		  Map<String, Object>	uploadResponse  = cloudinaryService.uploadFile(profileImg);
		  student.setProfilePublicId(uploadResponse.get("public_id").toString());
		  student.setProfileUrl(uploadResponse.get("secure_url").toString());
		}
		
		if (name != null) student.setName(name);
	    if (gender != null) student.setGender(gender);
	    if (mobile != null) student.setMobile(mobile);
	    if (whatsappNo != null) student.setWhatsappNo(whatsappNo);
	    if (dob != null) student.setDob(dob);
	    if (summary != null) student.setSummary(summary);
	    if (address != null) student.setAddress(address);
	    if (state != null) student.setState(state);
	    if (city != null) student.setCity(city);
	    if (experience != null) student.setExperience(experience);
	    if (skills != null) student.setSkills(skills);
	    if (tenthSchool != null) student.setTenthSchool(tenthSchool);
	    if (tenthPassOutYear != null) student.setTenthPassOutYear(tenthPassOutYear);
	    if (tenthPercentage != null) student.setTenthPercentage(tenthPercentage);
	    if (twelveSchool != null) student.setTwelveSchool(twelveSchool);
	    if (twelvePassOutYear != null) student.setTwelvePassOutYear(twelvePassOutYear);
	    if (twelvePercentage != null) student.setTwelvePercentage(twelvePercentage);
	    if (gradSchool != null) student.setGradSchool(gradSchool);
	    if (gradCourse != null) student.setGradCourse(gradCourse);
	    if (gradBranch != null) student.setGradBranch(gradBranch);
	    if (gradPercentage != null) student.setGradPercentage(gradPercentage);
	    if (gradCgpa != null) student.setGradCgpa(gradCgpa);
	    if (gradPassOutYear!=null) student.setGradPassOutYear(gradPassOutYear);
	    
	    studentService.updateStudent(student);
		return ResponseEntity.ok(new ApiResponse<>("success","Student Profile data",student));
			
	}
	
	
	@GetMapping("/secure/jd/{jobDescriptionId}")
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
		jobDescriptionDTO.setApprovedDate(jobDescription.getApprovedDate());
		jobDescriptionDTO.setManagerId(null);
		jobDescriptionDTO.setManagerName(null);


		return ResponseEntity.ok(new ApiResponse<>("success", "Job Description Fetched", jobDescriptionDTO));

	}
	
	@GetMapping("/secure/jd")
	@PreAuthorize("hasAnyRole('STUDENT')")
	public ResponseEntity<?> viewAllJobDescriptions(
			@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String companyName,
			@RequestParam(required = false, defaultValue = "") String stack,
			@RequestParam(required = false) String role, 
			@RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false, defaultValue = "") String qualification,
			@RequestParam(required = false, defaultValue = "") String stream,
			@RequestParam(required = false) Double percentage,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {

		String executiveId = null;
		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
		Page<JobDescription> jobDescriptions = jobDescriptionService.findAllJobDescriptions(companyName, stack, role,
				isClosed, minYearOfPassing, maxYearOfPassing, qualification, stream, percentage, executiveId, null,
				"approved", startDate, endDate, pageable);
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
			jobDescriptionDTO.setApprovedDate(jobDescription.getApprovedDate());
			jobDescriptionDTO.setManagerId(null);
			jobDescriptionDTO.setManagerName(null);
			return jobDescriptionDTO;
		});
		return ResponseEntity.ok(new ApiResponse<>("success", "Job Descriptions fetched successfully", JobDescriptionDTOResponse));
	}
	
	
	
}
