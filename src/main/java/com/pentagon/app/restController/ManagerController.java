package com.pentagon.app.restController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


import com.pentagon.app.entity.Executive;

import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.ManagerException;

import com.pentagon.app.requestDTO.AddExecutiveRequest;
import com.pentagon.app.requestDTO.AddTrainerRequest;
import com.pentagon.app.requestDTO.TrainerDTO;
import com.pentagon.app.requestDTO.UpdateManagerRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.PageResponse;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.utils.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/secure/updateManager")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateManager(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@Valid @RequestBody UpdateManagerRequest request, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			throw new ManagerException("Ivalid Input Data", HttpStatus.BAD_REQUEST);
		}
		
		if(managerDetails == null) {
			throw new ManagerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		Manager manager = managerDetails.getManager();
		
		manager.setName(request.getName());
		manager.setEmail(request.getEmail());
		manager.setMobile(request.getMobile());
		
		if (request.getPassword() != null && !request.getPassword().isBlank()) {
			
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            manager.setPassword(hashedPassword);
            
        }
		
		Manager updatedManager = managerService.updateManager(manager);
		
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Updated Successfully", null));
	}
	
	@PostMapping("secure/addExecutive")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> addExecutive(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@Valid @RequestBody AddExecutiveRequest request, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			throw new ManagerException("Invaid Input Data", HttpStatus.BAD_REQUEST );
		}
		
		if(managerDetails == null) {
			throw new ManagerException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		Executive executive = new Executive();
		executive.setName(request.getName());
		executive.setEmail(request.getEmail());
		executive.setMobile(request.getMobile());
		
        if(request.getPassword() != null && !request.getPassword().isBlank()) {
			
			String hashedPassword = passwordEncoder.encode(request.getPassword());
			executive.setPassword(hashedPassword);
		}
        
        Map<String, Object> claims = new HashMap<>();
		claims.put("email", executive.getEmail());
		claims.put("role", "EXECUTIVE");
		
		jwtUtil.generateToken(executive.getEmail(), claims);
		
		Executive newExecutive = managerService.addExecutive(executive);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Added Successfully", null));
	}
	
	@PostMapping("secure/addTrainer")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> addTrainer(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@Valid @RequestBody AddTrainerRequest request, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			throw new ManagerException("Invaid Input Data", HttpStatus.BAD_REQUEST );
		}
		
		if(managerDetails == null) {
			throw new ManagerException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		Trainer trainer = new Trainer();
		trainer.setName(request.getName());
		trainer.setEmail(request.getEmail());
		trainer.setMobile(request.getMobile());
		trainer.setTrainerStack(request.getTrainerStack());
		trainer.setTechnologies(request.getTechnologies());
		trainer.setYearOfExperiences(request.getYearOfExperiences());
		trainer.setQualification(request.getQualification());
		trainer.setAcitve(true);
		
		if(request.getPassword() != null && !request.getPassword().isBlank()) {
			
			String hashedPassword = passwordEncoder.encode(request.getPassword());
			trainer.setPassword(hashedPassword);
		}
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", trainer.getEmail());
		claims.put("role", "TRAINER");
		
		jwtUtil.generateToken(trainer.getEmail(), claims);
		
		Trainer newTrainer = managerService.addTrainer(trainer);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Added Successfully", null));
	}
	
	@GetMapping("/secure/viewAllTrainers")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<ApiResponse<PageResponse<TrainerDTO>>> viewAllTrainers(
			@RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int limit,
	        @RequestParam(required = false) String stack,
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String trainerId){
		
		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
		
		Page<Trainer> trainers = managerService.viewAllTrainers(stack, name, trainerId, pageable);
		
		Page<TrainerDTO> TrainerDTOPage = trainers.map(trainer -> {
            TrainerDTO dto = new TrainerDTO();
            dto.setId(trainer.getId());
            dto.setTrainerId(trainer.getTrainerId());
            dto.setName(trainer.getName());
            dto.setEmail(trainer.getEmail());
            dto.setMobile(trainer.getMobile());
            dto.setTrainerStack(trainer.getTrainerStack());
            dto.setQualification(trainer.getQualification());
            dto.setYearOfExperiences(trainer.getYearOfExperiences());
            dto.setTechnologies(trainer.getTechnologies());
            dto.setActive(trainer.isAcitve());
            dto.setCreatedAt(trainer.getCreatedAt());
            dto.setUpdatedAt(trainer.getUpdatedAt());
            return dto;
        });
		
		PageResponse<TrainerDTO> pageResponse = new PageResponse<>(TrainerDTOPage);
		
		return ResponseEntity.ok(
	            new ApiResponse<>("success", "Trainers fetched successfully", pageResponse)
	        );
	}
	
	
	
}
