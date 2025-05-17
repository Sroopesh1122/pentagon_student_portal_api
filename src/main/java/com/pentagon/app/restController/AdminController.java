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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.requestDTO.AddExecutiveRequest;
import com.pentagon.app.requestDTO.AddManagerRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.AdminService;
import com.pentagon.app.service.CustomUserDetails;
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

	@PostMapping("/secure/addManager")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addManagerByAdmin(@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody AddManagerRequest newManager,
			BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
			throw new AdminException("Invalid data ", HttpStatus.BAD_REQUEST);
		if(customUserDetails==null)
		{
			throw new AdminException("Unauthenticated", HttpStatus.UNAUTHORIZED);
		}
		Manager manager=new Manager();
		//manager.setManagerId(null);
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
		//executive.setExecutiveId(null);
		
		
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
	
}
