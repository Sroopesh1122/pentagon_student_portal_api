package com.pentagon.app.restController;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.requestDTO.AddExecutiveRequest;
import com.pentagon.app.requestDTO.AddManagerRequest;
import com.pentagon.app.requestDTO.TrainerDTO;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ProfileResponceDto;
import com.pentagon.app.response.PageResponse;
import com.pentagon.app.service.AdminService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.utils.JwtUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	
	@Autowired 
	AdminService adminservice;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired 
	private JwtUtil jwtUtil;
	@Autowired 
	private IdGeneration idGeneration;

	@Autowired
	private ManagerService managerService;

	@PostMapping("/secure/addManager")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addManagerByAdmin(@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody AddManagerRequest newManager,
			BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
			throw new AdminException("Invalid data", HttpStatus.BAD_REQUEST);
		if(customUserDetails==null)
		{
			throw new AdminException("Unauthenticated", HttpStatus.UNAUTHORIZED);
		}
		Manager manager=new Manager();
		manager.setManagerId(idGeneration.generateId("ADMIN"));
		manager.setName(newManager.getName());
		manager.setEmail(newManager.getEmail());
		manager.setMobile(newManager.getMobile());
		manager.setPassword(passwordEncoder.encode(newManager.getPassword()));
		manager.setActive(true);
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", manager.getEmail());
	    claims.put("role","MANAGER");
		
		jwtUtil.generateToken(manager.getEmail(), claims );
		
		adminservice.addManager(manager);
		return ResponseEntity.ok(new ApiResponse<>("success","Manager added Successfully",null));
	}
	
	@PostMapping("/secure/addExecutive")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addExecutiveByAdmin(@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody	AddExecutiveRequest newExecutive,
			BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
			throw new AdminException("Invalid data ", HttpStatus.BAD_REQUEST);
		if(customUserDetails==null)
		{
			throw new AdminException("Unauthorized",HttpStatus.UNAUTHORIZED);
		}
		Executive executive=new Executive();
		executive.setExecutiveId(idGeneration.generateId("EXECUTIVE"));
		executive.setName(newExecutive.getName());
		executive.setEmail(newExecutive.getEmail());
		executive.setActive(true);
		executive.setMobile(newExecutive.getMobile());
		executive.setPassword(passwordEncoder.encode(newExecutive.getPassword()));
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", executive.getEmail());
	    claims.put("role","MANAGER");
		jwtUtil.generateToken(executive.getEmail(), claims );
		adminservice.addExecutive(executive);
		return ResponseEntity.ok(new ApiResponse<>("success","Executive added Successfully",null));
	}
	

	@GetMapping("secure/profile")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAdminProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		if(customUserDetails == null) {
			throw new AdminException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
	    Admin admin= customUserDetails.getAdmin();
	    ProfileResponceDto details = adminservice.getProfile(admin);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Admin Profile", details));
	}

	@GetMapping("/secure/viewAllTrainers")
	@PreAuthorize("hasRole('ADMIN')")
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
