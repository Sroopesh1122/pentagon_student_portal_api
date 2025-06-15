package com.pentagon.app.restController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Student.EnrollmentStatus;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.StudentAdminException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.request.AddStudentRequest;
import com.pentagon.app.request.TrainerUpdateRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.service.TechnologyService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.utils.JwtUtil;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/trainer")
public class TrainerController {
	
	@Autowired
	private TrainerService trainerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private ActivityLogService activityLogService;
	
	@Autowired
	private IdGeneration idGeneration;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TechnologyService technologyService;

	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ManagerService managerService;
	
	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> getAdminProfile(@AuthenticationPrincipal CustomUserDetails trainerDetails) {
		if(trainerDetails.getTrainer() == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
	    Trainer trainer = trainerDetails.getTrainer();
	    ProfileResponse details = trainerService.getProfile(trainer);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Profile", details));
	}
	
	@PostMapping("/secure/updateTrainer")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> updateTrainer( @AuthenticationPrincipal CustomUserDetails trainerDetails,
            @Valid @RequestBody  TrainerUpdateRequest request,
            BindingResult bindingResult){
		
		if (bindingResult.hasErrors()) {
            throw new TrainerException("Invalid input data", HttpStatus.BAD_REQUEST);
        }
		
		if(trainerDetails.getTrainer() == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		Trainer trainer = trainerDetails.getTrainer();
		
		if (trainer == null) {
            throw new TrainerException("Trainer not found", HttpStatus.NOT_FOUND);
        }

		trainer.setName(request.getName());
		trainer.setEmail(request.getEmail());
		trainer.setMobile(request.getMobile());
		
		if (request.getPassword() != null && !request.getPassword().isBlank()) {
			
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            trainer.setPassword(hashedPassword);    
        }
		 
		Trainer updatedTrainer = trainerService.updateTrainer(trainer);
		
		activityLogService.log(trainerDetails.getTrainer().getEmail(), 
				trainerDetails.getTrainer().getTrainerId(), 
				"TRAINER", 
 				"Trainer with ID " + trainerDetails.getTrainer().getTrainerId() + "Updated his profile details");
        return  ResponseEntity.ok(new ApiResponse<>("success", "Profile Updated Successfully", null));
		
	}
	
	@PostMapping("/secure/submit-mockrating")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> submitMockRating(
	        @AuthenticationPrincipal CustomUserDetails trainerDetails,
	        @RequestParam String studentId,
	        @RequestParam String techId,
	        @RequestParam double mockRating) {

	    if (trainerDetails.getTrainer() == null) {
	        throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
	    }

	    Trainer trainer = trainerService.checkExistsByEmail(trainerDetails.getUsername());
	    if (trainer == null) {
	        throw new TrainerException("Trainer not found", HttpStatus.NOT_FOUND);
	    }

	    Student student = studentService.findByStudentId(studentId);
	    Technology tech = technologyService.findByTechId(techId); // create this method

	    trainerService.submitMockRating(trainer, student, tech, mockRating);

	    activityLogService.log(
	        trainer.getEmail(),
	        trainer.getTrainerId(),
	        "TRAINER",
	        "Trainer " + trainer.getName() + " submitted mock rating " + mockRating + 
	        " for student " + student.getName() + " on tech " + tech.getName()
	    );

	    return ResponseEntity.ok(new ApiResponse<>("success", "Mock Rating Submitted", null));
	}

	@GetMapping("/secure/students-by-batch")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> getStudentsByBatchId(
	        @AuthenticationPrincipal CustomUserDetails trainerDetails,
	        @RequestParam String batchId) {

	    if (trainerDetails.getTrainer() == null) {
	        throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
	    }

	    Trainer trainer = trainerService.checkExistsByEmail(trainerDetails.getUsername());
	    if (trainer == null) {
	        throw new TrainerException("Trainer not found", HttpStatus.NOT_FOUND);
	    }

	    List<Student> students = trainerService.getStudentsByBatch(batchId, trainer.getTrainerId());
	    return ResponseEntity.ok(new ApiResponse<>("success", "Students fetched", students));
	}

	@GetMapping("/secure/find-student")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> getStudentByIdForTrainer(
	        @AuthenticationPrincipal CustomUserDetails trainerDetails,
	        @RequestParam String studentId) {

	    if (trainerDetails.getTrainer() == null) {
	        throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
	    }

	    Trainer trainer = trainerService.checkExistsByEmail(trainerDetails.getUsername());
	    if (trainer == null) {
	        throw new TrainerException("Trainer not found", HttpStatus.NOT_FOUND);
	    }

	    Student student = trainerService.getStudentByIdIfTrainerAuthorized(trainer.getTrainerId(), studentId);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Student found", student));
	}

	@GetMapping("/secure/viewJobdescription")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> getAllJds(
			@AuthenticationPrincipal CustomUserDetails trainerDetails,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String companyName,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false, defaultValue = "") String qualification,
			@RequestParam(required = false, defaultValue = "") String stream,
			@RequestParam(required = false, defaultValue = "") String stack,
			@RequestParam(required = false) String role, @RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Double percentage, @RequestParam(required = false) String status,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate){
		
		if (trainerDetails.getTrainer() == null) {
			throw new TrainerException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}
		String trainerId= trainerDetails.getTrainer().getTrainerId();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").ascending());

		Page<JobDescription> filterdJobDiscription= jobDescriptionService.findAllJobDescriptions(companyName, stack, role, isClosed, minYearOfPassing, maxYearOfPassing, qualification, stream, percentage, null, null, status, startDate, endDate, pageable);
		Page<JobDescriptionDTO> jobDescriptionDTO = filterdJobDiscription.map(
			    job -> {
			    	JobDescriptionDTO dto = modelMapper.map(job, JobDescriptionDTO.class);
			    	dto.setManagerName(managerService.getManagerById(job.getManagerId()).getName());
			    	 return dto;
			    });
		
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Job Description Fetched", filterdJobDiscription));
	}
	
	@PostMapping("/secure/addStudent")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> addStudent(@AuthenticationPrincipal CustomUserDetails trainerDetails ,
			@Valid @RequestBody AddStudentRequest request,  BindingResult bindingResult ){
		
		if(bindingResult.hasErrors()) {
			throw new TrainerException("Invaid Input Data", HttpStatus.BAD_REQUEST );
		}
		
		if(trainerDetails.getTrainer() == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		Student student = new Student();
		student.setStudentId(idGeneration.generateStudentId(request.getStack(), request.getMode(), request.getTypeOfAdmission()));
		student.setName(request.getName());
		student.setEmail(request.getEmail());
		student.setMobile(request.getMobile());
		student.setStack(request.getStack());
//		student.setStatus(EnrollmentStatus.ACTIVE);
		student.setTypeOfAdmission(request.getTypeOfAdmission());
		
		if(request.getPassword() != null && !request.getPassword().isBlank()) {
			
			String hashedPassword = passwordEncoder.encode(request.getPassword());
			student.setPassword(hashedPassword);
		}
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", student.getEmail());
		claims.put("role", "STUDENT");
		
		jwtUtil.generateToken(student.getEmail(), claims);
		
		Student newStudent = studentService.addStudent(student);
		
		activityLogService.log(trainerDetails.getTrainer().getEmail(), 
				trainerDetails.getTrainer().getTrainerId(), 
				"TRAINER", 
				"Trainer with ID " + trainerDetails.getTrainer().getTrainerId() + "New Student added with Id "+ newStudent.getStudentId() );
		return ResponseEntity.ok(new ApiResponse<>("success", "Student Added Successfully", null));
		
	}	

}