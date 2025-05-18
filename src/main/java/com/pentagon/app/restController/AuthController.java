package com.pentagon.app.restController;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.requestDTO.AdminLoginRequest;
import com.pentagon.app.requestDTO.ExecutiveLoginRequest;
import com.pentagon.app.requestDTO.ManagerLoginRequest;
import com.pentagon.app.requestDTO.OtpVerificationRequest;
import com.pentagon.app.requestDTO.TrainerLoginRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.AdminService;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/pentagon/auth")
public class AuthController {

	private final AdminService adminService;
	private final ExecutiveService executiveService;
	private final ManagerService managerService;
	private final StudentService studentService;
	private final TrainerService trainerService;
	private final JwtUtil jwtUtil;

	public AuthController(AdminService adminService, ManagerService managerService, ExecutiveService executiveService, 
			TrainerService trainerService, StudentService studentService, 
			JwtUtil jwtUtil) {
		this.adminService = adminService;
		this.managerService = managerService;
		this.executiveService = executiveService;
		this.trainerService = trainerService;
		this.studentService = studentService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/public/admin/login")
	public ResponseEntity<?> adminLogin(@RequestBody @Valid AdminLoginRequest adminLoginRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new AdminException("Invalid details", HttpStatus.BAD_REQUEST);
		}
		String result = adminService.loginWithPassword(adminLoginRequest);
		return ResponseEntity.ok(new ApiResponse<>("success", result, null));
	}

	@PostMapping("/admin/verify-OTP")
	public ResponseEntity<?> adminOtpVerification(@RequestBody @Valid OtpVerificationRequest otpVerificationRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new AdminException("Invalid details", HttpStatus.BAD_REQUEST);
		}
		Boolean verified = adminService.verifyOtp(otpVerificationRequest);
		if (!verified) {
			throw new AdminException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", otpVerificationRequest.getEmail());
		claims.put("role", "ADMIN");

		String generatedToken = jwtUtil.generateToken(otpVerificationRequest.getEmail(), claims);

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("token", generatedToken);
		response.put("message", "LOGIN SUCCESSFULLY");

		return ResponseEntity.ok(new ApiResponse<>("success", "Login Successfully", response));
	}

	@PostMapping("/public/manager/login")
	public ResponseEntity<?> managerLogin(@RequestBody @Valid ManagerLoginRequest managerLoginRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ManagerException("Incalid details", HttpStatus.BAD_REQUEST);
		}
		String result = managerService.loginWithPassword(managerLoginRequest);
		return ResponseEntity.ok(new ApiResponse<>("success", result, null));
	}

	@PostMapping("/manager/verify-OTP")
	public ResponseEntity<?> managerOtpVerification(@RequestBody @Valid OtpVerificationRequest otpVerificationRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ManagerException("Invalid details", HttpStatus.BAD_REQUEST);
		}
		Boolean verified = managerService.verifyByOtp(otpVerificationRequest);
		if (!verified) {
			throw new ManagerException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", otpVerificationRequest.getEmail());
		claims.put("role", "MANAGER");

		String generatedToken = jwtUtil.generateToken(otpVerificationRequest.getEmail(), claims);

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("token", generatedToken);
		response.put("message", "LOGIN SUCCESSFULLY");

		return ResponseEntity.ok(new ApiResponse<>("success", "Login Successfully", response));
	}

	@PostMapping("/public/executive/login")
	public ResponseEntity<?> executiveLogin(@RequestBody @Valid ExecutiveLoginRequest executiveLoginRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ExecutiveException("Incalid details", HttpStatus.BAD_REQUEST);
		}
		String result = executiveService.loginWithPassword(executiveLoginRequest);
		return ResponseEntity.ok(new ApiResponse<>("success", result, null));
	}

	@PostMapping("/executive/verify-OTP")
	public ResponseEntity<?> ExecutiveOtpVerification(@RequestBody @Valid OtpVerificationRequest otpVerificationRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ExecutiveException("Invalid details", HttpStatus.BAD_REQUEST);
		}
		Boolean verified = adminService.verifyOtp(otpVerificationRequest);
		if (!verified) {
			throw new ExecutiveException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", otpVerificationRequest.getEmail());
		claims.put("role", "EXECUTIVE");

		String generatedToken = jwtUtil.generateToken(otpVerificationRequest.getEmail(), claims);

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("token", generatedToken);
		response.put("message", "LOGIN SUCCESSFULLY");

		return ResponseEntity.ok(new ApiResponse<>("success", "Login Successfully", response));
	}

	@PostMapping("/public/Trainer/login")
	public ResponseEntity<?> TrainerLogin(@RequestBody @Valid TrainerLoginRequest trainerLoginRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new TrainerException("Incalid details", HttpStatus.BAD_REQUEST);
		}
		String result = trainerService.loginWithPassword(trainerLoginRequest);
		return ResponseEntity.ok(new ApiResponse<>("success", result, null));
	}

	@PostMapping("/Trainer/verify-OTP")
	public ResponseEntity<?> TrainerOtpVerification(@RequestBody @Valid OtpVerificationRequest otpVerificationRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new TrainerException("Invalid details", HttpStatus.BAD_REQUEST);
		}
		Boolean verified = adminService.verifyOtp(otpVerificationRequest);
		if (!verified) {
			throw new TrainerException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", otpVerificationRequest.getEmail());
		claims.put("role", "TRAINER");

		String generatedToken = jwtUtil.generateToken(otpVerificationRequest.getEmail(), claims);

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("token", generatedToken);
		response.put("message", "LOGIN SUCCESSFULLY");

		return ResponseEntity.ok(new ApiResponse<>("success", "Login Successfully", response));
	}
	
}
