package com.pentagon.app.restController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.entity.Stack;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.StudentAdmin;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.entity.Student.EnrollmentStatus;
import com.pentagon.app.exception.StudentAdminException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.mapper.JobDescriptionMapper;
import com.pentagon.app.request.CreateBatchRequest;
import com.pentagon.app.request.CreateStudentRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.BatchService;
import com.pentagon.app.service.BatchTechTrainerService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.StackService;
import com.pentagon.app.service.StudentAdminService;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.service.TechnologyService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlTemplates;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.utils.PasswordGenration;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/studentAdmin")
public class StudentAdminController {
	@Autowired
	private JobDescriptionService jobDescriptionService;
	@Autowired
	private TrainerService trainerService;

	@Autowired
	private JobDescriptionMapper jobDescriptionMapper;

	@Autowired
	private BatchTechTrainerService batchTechTrainerService;

	@Autowired
	private StudentAdminService studentAdminService;

	@Autowired
	private TechnologyService technologyService;

	@Autowired
	private CloudinaryServiceImp cloudinaryService;

	@Autowired
	private StackService stackService;

	@Autowired
	private BatchService batchService;

	@Autowired
	private IdGeneration idGeneration;

	@Autowired
	private StudentService studentService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PasswordGenration passwordGenration;

	@Autowired
	private HtmlTemplates htmlTemplates;

	@Autowired
	private MailService mailServicel;

	@Value("${FRONTEND_URL}")
	private String FRONTEND_URL;

	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('STUDENTADMIN')")
	public ResponseEntity<?> getAdminProfile(@AuthenticationPrincipal CustomUserDetails adminDetails) {
		if (adminDetails.getStudentAdmin() == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}

		StudentAdmin studentAdmin = adminDetails.getStudentAdmin();
		return ResponseEntity.ok(new ApiResponse<>("success", "Student Admin Profile", studentAdmin));
	}

	@GetMapping("/secure/batch-info")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','ADMIN')")
	public ResponseEntity<?> getBatchInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Map<String, Object> batchInfo = new HashMap<>();

		Map<String, Object> batchCountInfo = new HashMap<>();

		Long completedBatches = batchService.countCompletedBatch();
		Long onGoingBatches = batchService.countOnGoingBatch();
		Long totalBatches = completedBatches + onGoingBatches;
		batchCountInfo.put("completed", completedBatches);
		batchCountInfo.put("onGoing", onGoingBatches);
		batchCountInfo.put("total", totalBatches);
		batchInfo.put("batchDetails", batchCountInfo);

		return ResponseEntity.ok(new ApiResponse<>("success", "Batch Info", batchInfo));

	}

	@GetMapping("/secure/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<?> getStudentAdmin(@PathVariable String id) {
		StudentAdmin studentAdmin = studentAdminService.getById(id);

		if (studentAdmin == null) {
			throw new StudentAdminException("Student Admin Not Found", HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(new ApiResponse<>("Success", "Student Admin profile", studentAdmin));

	}

	@PutMapping("/secure/block/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> blockAccount(@PathVariable String id) {
		StudentAdmin studentAdmin = studentAdminService.getById(id);
		if (studentAdmin == null) {
			throw new StudentAdminException("Account Not FOund", HttpStatus.NOT_FOUND);
		}

		studentAdmin.setActive(false);
		studentAdminService.update(studentAdmin);

		String mailMessage = htmlTemplates.getAccountBlockedEmail(studentAdmin.getName());

		mailServicel.sendAsync(studentAdmin.getEmail(), "Account Blocked - Pentagon Space", mailMessage);
		return ResponseEntity.ok(new ApiResponse<>("success", "Account Blocked Successfully", null));

	}

	@PutMapping("/secure/unblock/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> unBlockAccount(@PathVariable String id) {
		StudentAdmin studentAdmin = studentAdminService.getById(id);
		if (studentAdmin == null) {
			throw new StudentAdminException("Account Not FOund", HttpStatus.NOT_FOUND);
		}

		studentAdmin.setActive(true);
		studentAdminService.update(studentAdmin);

		String mailMessage = htmlTemplates.getAccountUnblockedEmail(studentAdmin.getName());

		mailServicel.sendAsync(studentAdmin.getEmail(), "Account Unb1locked - Pentagon Space", mailMessage);

		return ResponseEntity.ok(new ApiResponse<>("success", "Account Un-Blocked Successfully", null));

	}

	@GetMapping("/secure/student-info")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','ADMIN')")
	public ResponseEntity<?> getStudentInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Map<String, Object> studentInfo = new HashMap<>();

		Map<String, Object> studentCountInfo = new HashMap<>();

		Long activeStudents = studentService.countStudents(EnrollmentStatus.ACTIVE);
		Long blockedStudents = studentService.countStudents(EnrollmentStatus.BLOCKED);
		Long completedStudent = studentService.countStudents(EnrollmentStatus.COMPLETED);
		Long placedStudent = studentService.countStudents(EnrollmentStatus.PLACED);

		Long totalStudents = activeStudents + blockedStudents + completedStudent + placedStudent;

		studentCountInfo.put("active", activeStudents);
		studentCountInfo.put("blocked", blockedStudents);
		studentCountInfo.put("completed", completedStudent);
		studentCountInfo.put("placed", placedStudent);
		studentCountInfo.put("total", totalStudents);

		studentInfo.put("studentDetails", studentCountInfo);

		return ResponseEntity.ok(new ApiResponse<>("success", "Student Info", studentInfo));

	}

	@GetMapping("/secure/stack-batch-student-info")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','ADMIN')")
	public ResponseEntity<?> getStackInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Map<String, Object> stackInfo = new HashMap<>();

		List<Stack> stacks = stackService.getAll();

		stacks.forEach(stack -> {

			Map<String, Object> info = new HashMap<>();

			Map<String, Object> batchInfo = new HashMap<>();

			Long completedBatches = batchService.countCompletedBatchByStack(stack.getStackId());
			Long onGoingBatches = batchService.countOnGOingBatchByStack(stack.getStackId());
			Long totalBatched = completedBatches + onGoingBatches;

			batchInfo.put("completed", completedBatches);
			batchInfo.put("onGoing", onGoingBatches);
			batchInfo.put("total", totalBatched);

			Map<String, Object> studentCountInfo = studentService.countStudentByStack(stack.getStackId());

			info.put("batchInfo", batchInfo);
			info.put("studentInfo", studentCountInfo);

			stackInfo.put(stack.getName(), info);
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Stack Info", stackInfo));

	}

	@PutMapping("/secure")
	@PreAuthorize("hasRole('STUDENTADMIN')")
	public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@RequestParam(required = false) String mobile, @RequestPart(required = false) MultipartFile profileImg) {
		StudentAdmin studentAdmin = customUserDetails.getStudentAdmin();

		if (profileImg != null) {
			if (studentAdmin.getProfileImgPublicId() != null) {
				cloudinaryService.deleteFile(studentAdmin.getProfileImgPublicId(), "image");
			}
			Map<String, Object> uploadResponse = cloudinaryService.uploadImage(profileImg);
			studentAdmin.setProfileImgPublicId(uploadResponse.get("public_id").toString());
			studentAdmin.setProfileImgUrl(uploadResponse.get("secure_url").toString());
		}

		if (mobile != null) {
			studentAdmin.setMobile(mobile);
		}

		studentAdminService.update(studentAdmin);
		return ResponseEntity.ok(new ApiResponse<>("success", "Profile Updated Successfully", null));
	}

	@GetMapping("/public/jd")
	@PreAuthorize("hasRole('STUDENTADMIN')")
	public ResponseEntity<?> getAllJds(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String companyName, @RequestParam(required = false) String stack,
			@RequestParam(required = false) String role, @RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false) String qualification, @RequestParam(required = false) String stream,
			@RequestParam(required = false) Double percentage, @RequestParam(required = false) String status,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
		Page<JobDescriptionDTO> jobDescriptions = jobDescriptionService
				.findAllJobDescriptions(companyName, stack, role, isClosed, minYearOfPassing, maxYearOfPassing,
						qualification, stream, percentage, null, null, status, startDate, endDate, pageable)
				.map(jobDescription -> jobDescriptionMapper.toDTO(jobDescription));
		return ResponseEntity.ok(new ApiResponse<>("success", "Jd results", jobDescriptions));
	}

	@GetMapping("/secure/viewAllTrainers")
	@PreAuthorize("hasRole('STUDENTADMIN')")
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
			dto.setActive(trainer.isActive());
			dto.setCreatedAt(trainer.getCreatedAt());
			dto.setUpdatedAt(trainer.getUpdatedAt());
			return dto;
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers fetched successfully", TrainerDTOPage));
	}

	@PostMapping("/secure/batch/add")
	@PreAuthorize("hasRole('STUDENTADMIN')")
	@Transactional
	public ResponseEntity<?> createBatch(@Valid @RequestBody CreateBatchRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new StudentAdminException("Invalid Data", HttpStatus.BAD_REQUEST);

		}
		Stack findStack = stackService.getStackById(request.getStackId()).orElse(null);
		if (findStack == null) {
			throw new StudentAdminException("Stack Not Found", HttpStatus.NOT_FOUND);
		}

		String batchId = idGeneration.generateBatchId(findStack.getName());

		Batch findBatch = batchService.getBatchById(batchId).orElse(null);

		if (findBatch != null) {
			throw new StudentAdminException("Batch Already exists", HttpStatus.CONFLICT);

		}

		Batch newBatch = new Batch();
		newBatch.setBatchId(batchId);
		newBatch.setName(request.getBatchName());
		newBatch.setMode(request.getBatchMode());
		newBatch.setStack(findStack);
		Batch createdBatch = batchService.addBatch(newBatch);

		findStack.getTechnologies().forEach(technology -> {
			BatchTechTrainer batchTechTrainer = new BatchTechTrainer();
			batchTechTrainer.setBatch(createdBatch);
			batchTechTrainer.setCreatedAt(LocalDateTime.now());
			batchTechTrainer.setTechnology(technology);
			batchTechTrainer.setStatus("Not Started");
			batchTechTrainer = batchTechTrainerService.assignTrainer(batchTechTrainer);
		});

//		request.getScheduleDetails().forEach(schedule->{
//			boolean available = batchTechTrainerService.checkTrainerAvailabality(schedule.getTrainerId(), schedule.getStartTime(), schedule.getEndTime());
//			if(!available)
//			{
//				throw new StudentAdminException("Trainer unavailable between "+schedule.getStartTime()+" to "+schedule.getEndTime(), HttpStatus.CONFLICT);
//			}
//			Technology findTechnology = technologyService.getTechnologyById(schedule.getTechId()).orElse(null);
//			Trainer findTrainer = trainerService.getById(schedule.getTrainerId());
//			if(findTechnology==null)
//			{
//				throw new StudentAdminException("Technology Not Found", HttpStatus.CONFLICT);
//			}
//			if(findTrainer ==  null)
//			{
//				throw new StudentAdminException("Trainer Not Found", HttpStatus.CONFLICT);
//			}
//			BatchTechTrainer batchTechTrainer  = new BatchTechTrainer();
//			batchTechTrainer.setBatch(createdBatch);
//			batchTechTrainer.setCreatedAt(LocalDateTime.now());
//			batchTechTrainer.setEndTime(schedule.getEndTime());
//			batchTechTrainer.setStartTime(schedule.getStartTime());
//			batchTechTrainer.setTechnology(findTechnology);
//			batchTechTrainer.setTrainer(findTrainer);
//			batchTechTrainer = batchTechTrainerService.assignTrainer(batchTechTrainer);
//		});	

		return ResponseEntity.ok(new ApiResponse<>("success", "Batch Created Successfully", null));

	}

	@PostMapping("/secure/student/add")
	@PreAuthorize("hasRole('STUDENTADMIN')")
	public ResponseEntity<?> createStudentAccount(@RequestBody @Valid CreateStudentRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new StudentAdminException("Invalid Data", HttpStatus.BAD_REQUEST);
		}

		Stack findStack = stackService.getStackById(request.getStackId()).orElse(null);
		if (findStack == null) {
			throw new StudentAdminException("Stack Not Found", HttpStatus.NOT_FOUND);
		}

		Batch findBatch = batchService.getBatchById(request.getBatchId()).orElse(null);
		if (findBatch == null) {
			throw new StudentAdminException("Batch Not Found", HttpStatus.NOT_FOUND);
		}

		Student findStudent = studentService.findByEmail(request.getEmail());
		if (findStudent != null) {
			throw new StudentAdminException("Email Already exists", HttpStatus.CONFLICT);
		}

		String stackCode = "";

		switch (findStack.getName()) {
		case "Java Full Stack": {
			stackCode = "java full stack";
			break;
		}
		case "Python Full Stack": {
			stackCode = "python full stack";
			break;
		}
		case "Mern Full Stack": {
			stackCode = "mern full stack";
			break;
		}
		case "Software Testing": {
			stackCode = "software testing";
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + findStack);
		}

		Student newStudent = new Student();
		newStudent.setStudentId(
				idGeneration.generateStudentId(stackCode, request.getMode(), request.getAdmissionMode(), findBatch));
		newStudent.setName(request.getFullName());
		newStudent.setStack(findStack);
		newStudent.setBatch(findBatch);
		newStudent.setEmail(request.getEmail());
		newStudent.setMobile(request.getMobile());
		newStudent.setStudyMode(request.getMode());
		newStudent.setTypeOfAdmission(request.getAdmissionMode());
		String generatedPassword = passwordGenration.generateRandomPassword();
		newStudent.setPassword(passwordEncoder.encode(generatedPassword));
		newStudent.setStatus(EnrollmentStatus.ACTIVE);
		newStudent.setValidUpto(LocalDate.now().plusYears(1));

		String passwordResetToken = idGeneration.generateRandomString();

		newStudent.setPasswordResetToken(passwordResetToken);

		newStudent = studentService.addStudent(newStudent);

		String passwordResetLink = FRONTEND_URL + "/auth/student/reset-password?token=" + passwordResetToken;

		String accountCreatedHtmlTemplates = htmlTemplates.getAccountCreatedEmail(newStudent.getName(),
				passwordResetLink);

		try {
			mailServicel.send(newStudent.getEmail(), "Account Created", accountCreatedHtmlTemplates);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Student Added Successfully", newStudent));

	}

}
