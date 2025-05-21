package com.pentagon.app.restController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Student.EnrollmentStatus;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.requestDTO.AddStudentRequest;
import com.pentagon.app.requestDTO.TrainerUpdateRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ProfileResponceDto;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.TrainerService;
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
	
	
	@PostMapping("/secure/updateTrainer")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> updateTrainer( @AuthenticationPrincipal CustomUserDetails trainerDetails,
            @Valid @RequestBody  TrainerUpdateRequest request,
            BindingResult bindingResult){
		
		if (bindingResult.hasErrors()) {
            throw new TrainerException("Invalid input data", HttpStatus.BAD_REQUEST);
        }
		
		if(trainerDetails == null) {
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

        return  ResponseEntity.ok(new ApiResponse<>("success", "Profile Updated Successfully", null));
		
	}
	
	@PostMapping("/secure/addStudent")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> addStudent(@AuthenticationPrincipal CustomUserDetails trainerDetails ,
			@Valid @RequestBody AddStudentRequest request,  BindingResult bindingResult ){
		
		if(bindingResult.hasErrors()) {
			throw new TrainerException("Invaid Input Data", HttpStatus.BAD_REQUEST );
		}
		
		if(trainerDetails == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		Student student = new Student();
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
		
		Student newStudent = trainerService.addStudent(student);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Student Added Successfully", null));
		
	}
	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> getAdminProfile(@AuthenticationPrincipal CustomUserDetails trainerDetails) {
		if(trainerDetails == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
	    Trainer trainer = trainerDetails.getTrainer();
	    ProfileResponceDto details = trainerService.getProfile(trainer);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Profile", details));
	}
	
	

}