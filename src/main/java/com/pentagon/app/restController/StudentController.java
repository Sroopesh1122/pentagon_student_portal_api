package com.pentagon.app.restController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
import com.pentagon.app.entity.Technology;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.StudentException;
import com.pentagon.app.mapper.StudentMapper;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.MockTestService;
import com.pentagon.app.service.StackService;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.service.TechnologyService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;

import jakarta.transaction.Transactional;

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
	
	@Autowired
	private MockTestService mockTestService;
	
	
	@Autowired
	private TechnologyService technologyService;
	
	@Autowired
	private StackService stackService;
	
	
	@GetMapping("/secure/profile")
	public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails)
	{
		Student student = customUserDetails.getStudent();
		
		StudentDTO studentDTO =  studentMapper.toDto(student);
		
		return ResponseEntity.ok(new ApiResponse<>("success","Student Profile data",studentDTO));
			
	}
	
	
	@GetMapping("/secure/all")
	public ResponseEntity<?> getStudent(
			@RequestParam(required = false ,defaultValue = "0") Integer page,
			@RequestParam(required = false,defaultValue = "10") Integer limit,
			@RequestParam(required = false) String batchId,
			@RequestParam(required = false) String stackId,
			@RequestParam(required = false) String q)
	{
		
		Pageable pageable = PageRequest.of(page, limit,Sort.by("createdAt").ascending());
		
		Page<StudentDTO> students = studentService.findStudent(q, batchId, stackId,pageable).map(student->studentMapper.toDto(student));
		
		
		return ResponseEntity.ok(new ApiResponse<>("success","Student Profile data",students));
			
	}
	
	@GetMapping("/secure/counts")
	public ResponseEntity<?> getStudentCount(
			@RequestParam(required = false) String batchId,
			@RequestParam(required = false) String stackId)
	{
		Map<String, Long> studentStats = studentService.countStudent(batchId, stackId);
		return ResponseEntity.ok(new ApiResponse<>("success","Student Count", studentStats));
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
		    @RequestPart(required = false) MultipartFile profileImg,
		    @RequestParam(required =  false) String github,
		    @RequestParam(required =  false) String linkedin,
		    @RequestPart(required = false) MultipartFile resumeFile
		    )
	{
		Student student = customUserDetails.getStudent();
		
		if(profileImg!=null)
		{
			if(student.getProfilePublicId()!=null)
			{
				cloudinaryService.deleteFile(student.getProfilePublicId(),"image");
			}
		  Map<String, Object>	uploadResponse  = cloudinaryService.uploadImage(profileImg);
		  student.setProfilePublicId(uploadResponse.get("public_id").toString());
		  student.setProfileUrl(uploadResponse.get("secure_url").toString());
		}
		
		if(resumeFile !=null)
		{
			if(student.getResumePublicId() !=null)
			{
				cloudinaryService.deleteFile(student.getResumePublicId(),"raw");
			}
			Map<String, Object>	uploadResponse  = cloudinaryService.uploadPdf(resumeFile);
			student.setResumePublicId(uploadResponse.get("public_id").toString());
			student.setResumeUrl(uploadResponse.get("secure_url").toString());
			
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
	    if(github !=null) student.setGithub(github);
	    if(linkedin!=null) student.setLinkedin(linkedin);
	    
	    
	    
	    studentService.updateStudent(student);
		return ResponseEntity.ok(new ApiResponse<>("success","Student Profile data",student));
			
	}
	
	
	@GetMapping("/secure/jd/{jobDescriptionId}")
	@PreAuthorize("hasAnyRole('TRAINER','EXECUTIVE','MANAGER')")
	public ResponseEntity<?> getJobDescriptionById(@AuthenticationPrincipal CustomUserDetails customUserDetails ,@PathVariable String jobDescriptionId) {
		
		
		if(customUserDetails.getStudent() == null)
		{
			throw new StudentException("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		Student student = customUserDetails.getStudent();
		

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
		jobDescriptionDTO.setAboutCompany(jobDescription.getAboutCompany());
		jobDescriptionDTO.setInterviewDate(jobDescription.getInterviewDate());
		jobDescriptionDTO.setGenderPreference(jobDescription.getGenderPreference());
		jobDescriptionDTO.setRolesAndResponsibility(jobDescription.getRolesAndResponsibility());
		jobDescriptionDTO.setGeneric(jobDescription.getGeneric());
		
		//Returns profile matched result
		Map<String,String> matchResult = profileMatch(jobDescription, student);
	

		jobDescriptionDTO.setStudentProfileMatch(matchResult);

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
			jobDescriptionDTO.setAboutCompany(jobDescription.getAboutCompany());
			jobDescriptionDTO.setInterviewDate(jobDescription.getInterviewDate());
			jobDescriptionDTO.setGenderPreference(jobDescription.getGenderPreference());
			jobDescriptionDTO.setRolesAndResponsibility(jobDescription.getRolesAndResponsibility());
			jobDescriptionDTO.setGeneric(jobDescription.getGeneric());
			return jobDescriptionDTO;
			
		});
		return ResponseEntity.ok(new ApiResponse<>("success", "Job Descriptions fetched successfully", JobDescriptionDTOResponse));
	}
	
	
	@Transactional
	private Map<String, String> profileMatch(JobDescription jobDescription, Student student) {
	    Map<String, String> matchResult = new LinkedHashMap<>();
	    
	    if(jobDescription.getGeneric()!=null)
	    {
	    	if(jobDescription.getGeneric().toLowerCase().equals("yes"))
	    	{
	    		matchResult.put("allMatched","Eligible");
	    		return matchResult;
	    	}
	    }
	    

	    // Check for nulls in required student fields
	    if (
	        student.getGender() == null ||
	        student.getGradCourse() == null ||
	        student.getGradBranch() == null ||
	        student.getStack() == null || student.getStack().getName() == null ||
	        student.getGradPassOutYear() == null ||
	        student.getTenthPercentage() == null ||
	        student.getGradPercentage() == null ||
	        student.getGradCgpa() == null ||
	        student.getTwelvePercentage() == null
	    ) {
	        matchResult.put("error", "Please complete your profile to apply for this job.");
	        return matchResult;
	    }

	    boolean allMatched = true;

	    // Gender
	    if (
	        jobDescription.getGenderPreference().equalsIgnoreCase("any") ||
	        jobDescription.getGenderPreference().equalsIgnoreCase(student.getGender())
	    ) {
	        matchResult.put("gender", "Eligible");
	    } else {
	        matchResult.put("gender", "Not Eligible");
	        allMatched = false;
	    }

	    // Qualification
	    if (
	        jobDescription.getQualification().toLowerCase().contains("any") ||
	        jobDescription.getQualification().toLowerCase().contains(student.getGradCourse().toLowerCase())
	    ) {
	        matchResult.put("qualification", "Eligible");
	    } else {
	        matchResult.put("qualification", "Not Eligible");
	        allMatched = false;
	    }

	    // Stream
	    if (
	        jobDescription.getStream().toLowerCase().contains("any") ||
	        jobDescription.getStream().toLowerCase().contains(student.getGradBranch().toLowerCase())
	    ) {
	        matchResult.put("stream", "Eligible");
	    } else {
	        matchResult.put("stream", "Not Eligible");
	        allMatched = false;
	    }

	    // Stack
	    if (
	        jobDescription.getStack().toLowerCase().contains("any") ||
	        jobDescription.getStack().toLowerCase().equals(student.getStack().getName().toLowerCase())
	    ) {
	        matchResult.put("stack", "Eligible");
	    } else {
	        matchResult.put("stack", "Not Eligible");
	        allMatched = false;
	    }

	    // Passing Year
	    Integer maxYearOfPassout = jobDescription.getMaxYearOfPassing();
	    Integer minYearOfPassout = jobDescription.getMinYearOfPassing();
	    Integer studentPassingYear = student.getGradPassOutYear();

	    if (
	        (maxYearOfPassout == -1 && minYearOfPassout == -1) ||
	        (minYearOfPassout <= studentPassingYear && studentPassingYear <= maxYearOfPassout)
	    ) {
	        matchResult.put("passingYear", "Eligible");
	    } else {
	        matchResult.put("passingYear", "Not Eligible");
	        allMatched = false;
	    }

	    // Percentages
	    Double _10thPercentage = student.getTenthPercentage();
	    Double _12thPercentage = student.getTwelvePercentage();
	    Double _gradPercentage = student.getGradPercentage();

	    if (_10thPercentage >= jobDescription.getPercentage() &&
	        _12thPercentage >= jobDescription.getPercentage() &&
	        _gradPercentage >= jobDescription.getPercentage()) {
	        matchResult.put("percentage", "Eligible");
	    } else {
	        matchResult.put("percentage", "Not Eligible");
	        allMatched = false;
	    }
	    
	    
	    //Skills 
	   
	    String[] jobSkills = jobDescription.getSkills().toLowerCase().split(",");
	    String[] profileSkills = student.getSkills().toLowerCase().split(",");

	    // Trim spaces
	    Set<String> profileSkillSet = new HashSet<>();
	    for (String skill : profileSkills) {
	        profileSkillSet.add(skill.trim());
	    }

	    int matchedSkills = 0;
	    for (String skill : jobSkills) {
	        if (profileSkillSet.contains(skill.trim())) {
	            matchedSkills++;
	        }
	    }
	    int totalJobSkills = jobSkills.length;
	    matchResult.put("skillsMatched", matchedSkills + " out of " + totalJobSkills + " job skills matched");
	    matchResult.put("matchedSkillsCount",matchedSkills+"");
	    matchResult.put("totalSkills", totalJobSkills+"");

	    // CGPA (uncomment if needed)
	    // Double _gradeCgpa = student.getGradCgpa();
	    // if (jobDescription.getCgpaRequirement() != null) {
	    //     if (_gradeCgpa >= jobDescription.getCgpaRequirement()) {
	    //         matchResult.put("cgpa", "Eligible");
	    //     } else {
	    //         matchResult.put("cgpa", "Not Eligible");
	    //         allMatched = false;
	    //     }
	    // }
	    
	    List<Technology> technologies = technologyService.findTechnologiesByStack(student.getStack().getStackId());
	    
	    String studentId = student.getStudentId();
	    
	    for (Technology technology : technologies) {
	        List<Double> ratings = mockTestService.getRatingOfStudent(studentId, technology.getTechId());
	        if (ratings != null && !ratings.isEmpty()) {
	            // Calculate average using streams
	            double avgRating = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
	            
	            System.out.println(technology.getName()+"  "+ avgRating);
	            
	            if(avgRating >= jobDescription.getMockRating())
	            {
	            	 matchResult.put(technology.getName()+" Rating", "Eligible");
	            }else {
	            	allMatched=false;
	            	matchResult.put(technology.getName()+" Rating", "Not Eligible");
	            }
	           
	        } else {
	        	allMatched=false;
	        	matchResult.put(technology.getName()+" Rating","Not Eligible"); // or null, depending on your use case
	        }
	        
	    }
	    
	    
	 
	    // Set allMatched
	    matchResult.put("allMatched", allMatched ? "Eligible" : "Not Eligible");

	    return matchResult;
	}
	
	
}
