package com.pentagon.app.restController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Student.EnrollmentStatus;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.mapper.TrainerMapper;
import com.pentagon.app.request.AddStudentRequest;
import com.pentagon.app.request.TrainerUpdateRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.BatchTechTrainerService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.StudentService;
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
	private BatchTechTrainerService batchTechTrainerService;
	
	@Autowired
	private TrainerMapper trainerMapper;
	
	
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
	
	
	@GetMapping("/public/trainer/{id}/schedule")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> getTrainerScheduleInfo(@AuthenticationPrincipal CustomUserDetails trainerDetails , @PathVariable String id) {
		
		
		Trainer findTrainer = trainerService.getById(id);
		
		if(findTrainer ==null)
		{
			throw new TrainerException("Trainer Not Found",HttpStatus.NOT_FOUND);
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
			@RequestParam(required = false) String q) {
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

		
		//if we pass null then all trainers are fetched under every program Head
		Page<TrainerDTO> trainers = trainerService.getAllTrainers(null,q,
				pageable).map(trainer -> trainerMapper.toDTO(trainer));

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers data", trainers));
	}
	
	//individual trainers
	@GetMapping("/secure/{id}")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','ADMIN','PROGRAMHEAD')")
	public ResponseEntity<?> getTrainerById(@PathVariable String id){
		
		Trainer findTrainer = trainerService.getById(id);
		
		if(findTrainer == null)
		{
			throw new TrainerException("Trainer not found", HttpStatus.NOT_FOUND);
		}
		
		TrainerDTO trainer = trainerMapper.toDTO(findTrainer);

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer data", trainer));
	}
	
	

}