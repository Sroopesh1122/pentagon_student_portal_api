package com.pentagon.app.restController;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.StudentJdApplcationDTO;
import com.pentagon.app.entity.ApplicationStatusHistory;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.StudentJdApplication;
import com.pentagon.app.exception.StudentJdApplcationException;
import com.pentagon.app.mapper.StudentJdApplcationMapper;
import com.pentagon.app.request.JdApplicationApplyRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.ApplicationStatusHistoryService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.StudentJdApplicationService;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlTemplates;
import com.pentagon.app.utils.IdGeneration;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/application")
public class StudentJdApplicationController {
	
	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private StudentJdApplicationService studentJdApplicationService;
	
	@Autowired
	private IdGeneration idGeneration;
	
	@Autowired
	private HtmlTemplates htmlTemplates;
	
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private StudentJdApplcationMapper studentJdApplcationMapper;
	
	@Autowired
	private ApplicationStatusHistoryService applicationStatusHistoryService;
	
	@Value("${FRONTEND_URL}")
	private String FRONTEND_URL;
	
	@Transactional
	@PostMapping("/secure/jd/apply")
	public ResponseEntity<?> applyApplicationForJd(@RequestBody @Valid JdApplicationApplyRequest request,BindingResult bindingResult) 
	{
		if(bindingResult.hasErrors())
		{
			throw new StudentJdApplcationException("Invalid Data", HttpStatus.NOT_FOUND);
		}
			
		Student student = studentService.findById(request.getStudentId());
		
		if(student == null)
		{
			throw new StudentJdApplcationException("Student Not Found", HttpStatus.NOT_FOUND);
		}
		JobDescription jobDescription = jobDescriptionService.finById(request.getJdId());
		
		if(jobDescription == null)
		{
			throw new StudentJdApplcationException("Jd Not Found", HttpStatus.NOT_FOUND);	
		}
		
		if(jobDescription.isClosed())
		{
			throw new StudentJdApplcationException("Jd is already closed", HttpStatus.NOT_FOUND);
		}
		
		if(jobDescription.getCurrentRegistrations() >= jobDescription.getNumberOfRegistrations())
		{
			throw new StudentJdApplcationException("Registration is closed", HttpStatus.NOT_FOUND);
		}
		
		StudentJdApplication studentJdApplication = studentJdApplicationService.findByStudentAndJd(student.getStudentId(), jobDescription.getJobDescriptionId());
		
		if(studentJdApplication !=null)
		{
			throw new StudentJdApplcationException("Application Already Applied", HttpStatus.BAD_REQUEST);
		}
		
		
		// Looking for match
		//later need to check for mock rating also
		String profileStack  = student.getStack().getName();
		String profileQualification = student.getGradCourse();
		String profileStream = student.getGradBranch();
		Double profile10thPercantage = student.getTenthPercentage();
		Double profile12thPercentage = student.getTwelvePercentage();
		Double profileGradPercentage = student.getGradPercentage();
		
		if(profileQualification == null || profileStream ==null || profile10thPercantage ==null || profile12thPercentage ==null || profileGradPercentage ==null)
		{
			throw new StudentJdApplcationException("Please Complete your profile to apply", HttpStatus.BAD_REQUEST);
		}
		
		
		Boolean isProfileMatchedJd = jdAndProfileMatch(student, jobDescription);
		
		
		if(!isProfileMatchedJd)
		{
			throw new StudentJdApplcationException("Your Profile Not Matched for this Jd", HttpStatus.BAD_REQUEST);
		}
		
		
		StudentJdApplication createApplication = new StudentJdApplication();
		String applicationId = "APPLICATION-"+idGeneration.generateRandomString();
		
		createApplication.setApplicationId(applicationId);
		createApplication.setCurrentStatus("Applied");
		createApplication.setCurrentRound("Not Scheduled");
		createApplication.setJobDescription(jobDescription);
		createApplication.setStudent(student);
		
		 createApplication= studentJdApplicationService.create(createApplication);
		 
		 
		 //Creating histort traces
		 
		 ApplicationStatusHistory applicationStatusHistory = new ApplicationStatusHistory();
		 applicationStatusHistory.setStudentJdApplication(createApplication);
		 applicationStatusHistory.setRound("Not Scheduled");
		 applicationStatusHistory.setStatus("Applied");
		 
		 applicationStatusHistoryService.create(applicationStatusHistory);
		
		 jobDescription.setCurrentRegistrations(jobDescription.getCurrentRegistrations()+1);
		 jobDescriptionService.updateJobDescription(jobDescription);
		 
		 String applicationViewLink = FRONTEND_URL +"/student/application/id="+applicationId;
		 
		 String htmlEMailTemplate = htmlTemplates.getStudentJdAppliedEmail(student.getName(), 
				 jobDescription.getRole(),
				 jobDescription.getCompanyName(),
				 jobDescription.getSalaryPackage(),
				 applicationId, 
				 applicationViewLink);
		 
		 try {
			mailService.send(student.getEmail(),"JD Application Has Been Submitted", htmlEMailTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return ResponseEntity.ok(new ApiResponse<>("success","Application applied Successfullt",null));
	}
	
	@GetMapping("/secure/details")
	public ResponseEntity<?> getApplcationByStudentAndJd(@RequestParam String studentId, @RequestParam String jdId )
	{
		StudentJdApplication studentJdApplication =  studentJdApplicationService.findByStudentAndJd(studentId, jdId);
		
		StudentJdApplcationDTO studentJdApplcationDTO = studentJdApplication !=null ? studentJdApplcationMapper.toDto(studentJdApplication) : null; 
		
		return ResponseEntity.ok(new ApiResponse<>("success","Application", studentJdApplcationDTO));
	}
	
	@GetMapping("/secure/{applicationId}/details")
	public ResponseEntity<?> getApplcationById(@PathVariable String applicationId )
	{
		
		StudentJdApplication studentJdApplication = studentJdApplicationService.findByApplicationId(applicationId);
		
		if(studentJdApplication ==null)
		{
			throw new StudentJdApplcationException("Application Not  Found", HttpStatus.NOT_FOUND);
		}
		
		StudentJdApplcationDTO studentJdApplcationDTO = studentJdApplcationMapper.toDto(studentJdApplication);
		
		return ResponseEntity.ok(new ApiResponse<>("success","Application Details", studentJdApplcationDTO));
		
	}
	
	
	@GetMapping("/secure/jd")
	public ResponseEntity<?> getApplcationByJd(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@RequestParam(required = false ,defaultValue = "0") Integer page,
			@RequestParam(required = false ,defaultValue = "12") Integer limit,
			@RequestParam String jdId,
			@RequestParam(required = false) String round,
			@RequestParam(required = false) String status
			)
	{
		
		
		Pageable pageable =   PageRequest.of(page, limit, Sort.by("appliedAt").descending());
		
		
		Page<StudentJdApplcationDTO> studentJdApplication =  studentJdApplicationService.getAllStudentAppliedForJd(jdId,round,status, pageable).map(application->studentJdApplcationMapper.toDto(application));
		
		
		return ResponseEntity.ok(new ApiResponse<>("success","JD Applicants", studentJdApplication));
	}
	
	
	
	@GetMapping("/secure/student")
	public ResponseEntity<?> getApplcationByStudent(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@RequestParam(required = false ,defaultValue = "0") Integer page,
			@RequestParam(required = false ,defaultValue = "12") Integer limit)
	{
		
		Student student = customUserDetails.getStudent();
		
		String studentId = student.getStudentId();
		
		
		Pageable pageable =   PageRequest.of(page, limit, Sort.by("appliedAt").descending());
		
		
		Page<StudentJdApplcationDTO> studentJdApplication =  studentJdApplicationService.getAllAppliedJdForStudent(studentId, pageable).map(application->studentJdApplcationMapper.toDto(application));
		
		
		return ResponseEntity.ok(new ApiResponse<>("success","Application", studentJdApplication));
	}
	
	
	
	
	
	
	
	
	//CHeck for jd and student profile match
	private Boolean jdAndProfileMatch(Student student, JobDescription jd) {

	    String profileStack = student.getStack().getName();
	    String profileQualification = student.getGradCourse();
	    String profileStream = student.getGradBranch();
	    Double profile10thPercentage = student.getTenthPercentage();
	    Double profile12thPercentage = student.getTwelvePercentage();
	    Double profileGradPercentage = student.getGradPercentage();
	    Integer yearOfPassing = student.getGradPassOutYear();

	    String jdStacks = jd.getStack(); // e.g. "Java, React"
	    String jdQualifications = jd.getQualification();
	    String jdStreams = jd.getStream();
	    Double jdPercentage = jd.getPercentage();
	    Integer jdMinYearPassing = jd.getMinYearOfPassing();
	    Integer jdMaxYearPassing = jd.getMaxYearOfPassing();

	    // Split and trim
	    List<String> jdStackList = Arrays.stream(jdStacks.split(","))
	        .map(String::trim)
	        .map(String::toLowerCase)
	        .toList();

	    List<String> jdQualificationList = Arrays.stream(jdQualifications.split(","))
	        .map(String::trim)
	        .map(String::toLowerCase)
	        .toList();

	    List<String> jdStreamList = Arrays.stream(jdStreams.split(","))
	        .map(String::trim)
	        .map(String::toLowerCase)
	        .toList();

	    boolean stackMatched = jdStackList.contains("any") ||
	                           jdStackList.contains(profileStack.toLowerCase());

	    boolean qualificationMatched = jdQualificationList.contains("any") ||
	                                   jdQualificationList.contains(profileQualification.toLowerCase());

	    boolean streamMatched = jdStreamList.contains("any") ||
	                            jdStreamList.contains(profileStream.toLowerCase());

	    boolean percentageMatched = (jdPercentage == 0) ||
	                                (profile10thPercentage >= jdPercentage &&
	                                 profile12thPercentage >= jdPercentage &&
	                                 profileGradPercentage >= jdPercentage);

	    boolean yearMatched = 
	    	    ((jdMinYearPassing != null && jdMinYearPassing == -1) &&
	    	     (jdMaxYearPassing != null && jdMaxYearPassing == -1))
	    	    ||
	    	    (yearOfPassing != null &&
	    	     (jdMinYearPassing == null || yearOfPassing >= jdMinYearPassing) &&
	    	     (jdMaxYearPassing == null || yearOfPassing <= jdMaxYearPassing));


	    return stackMatched && qualificationMatched && streamMatched && percentageMatched && yearMatched;
	}

	
	
	
}
