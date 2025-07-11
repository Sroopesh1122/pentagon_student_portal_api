package com.pentagon.app.restController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.ProgramHead;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.StudentAdmin;
import com.pentagon.app.entity.StudentJdApplication;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.*;
import com.pentagon.app.repository.StudentJdApplcationRepository;
import com.pentagon.app.request.*;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.*;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlTemplates;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.utils.JwtUtil;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AdminService adminService;
	private final ExecutiveService executiveService;
	private final ManagerService managerService;
	private final StudentService studentService;
	private final TrainerService trainerService;
	private final StudentAdminService studentAdminService;
	private final ProgramHeadService programHeadService;
	private final OtpService otpService;
	private final JwtUtil jwtUtil;
	private final ActivityLogService activityLogService;
	private final MailService mailService;
	private final PasswordEncoder passwordEncoder;
	private final IdGeneration idGeneration;
	private final HtmlTemplates htmlTemplates;

	@Autowired
	private StudentJdApplcationRepository studentJdApplcationRepository;

	@Value("${FRONTEND_URL}")
	private String FRONTEND_URL;

	public AuthController(AdminService adminService, ManagerService managerService, ExecutiveService executiveService,
			TrainerService trainerService, StudentAdminService studentAdminService,
			ProgramHeadService programHeadService, StudentService studentService, OtpService otpService,
			JwtUtil jwtUtil, ActivityLogService activityLogService, MailService mailService,
			PasswordEncoder passwordEncoder, IdGeneration idGeneration, HtmlTemplates htmlTemplates) {
		this.adminService = adminService;
		this.managerService = managerService;
		this.executiveService = executiveService;
		this.trainerService = trainerService;
		this.studentAdminService = studentAdminService;
		this.programHeadService = programHeadService;
		this.studentService = studentService;
		this.otpService = otpService;
		this.jwtUtil = jwtUtil;
		this.activityLogService = activityLogService;
		this.mailService = mailService;
		this.passwordEncoder = passwordEncoder;
		this.idGeneration = idGeneration;
		this.htmlTemplates = htmlTemplates;
	}

	private ResponseEntity<?> handleLogin(String role, String result) {
		return ResponseEntity.ok(new ApiResponse<>("success", result, null));
	}

	@PostMapping("/public/signin/verify-otp")
	public ResponseEntity<?> handleOtpVerification(@Valid @RequestBody OtpVerificationRequest request,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
		}

		boolean isVerified = otpService.verifyOtp(request);

		if (!isVerified) {
			String role = request.getRole().toUpperCase();

			switch (role) {
			case "ADMIN" -> throw new AdminException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
			case "MANAGER" -> throw new ManagerException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
			case "EXECUTIVE" -> throw new ExecutiveException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
			case "TRAINER" -> throw new TrainerException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
			case "STUDENT" -> throw new StudentException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
			case "STUDENTADMIN" -> throw new StudentAdminException("otp is Invalid/Expired", HttpStatus.UNAUTHORIZED);
			case "PROGRAMHEAD" -> throw new ProgramHeadException("otp is Invalid/Expired", HttpStatus.UNAUTHORIZED);
			default -> throw new UserException("Unknown role: " + role, HttpStatus.UNAUTHORIZED);
			}
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", request.getEmail());
		claims.put("role", request.getRole());
		String token = jwtUtil.generateToken(request.getEmail(), claims);

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("token", token);
		response.put("Role", request.getRole());
		response.put("message", "LOGIN SUCCESSFULLY");
		activityLogService.log(request.getEmail(), null, request.getRole(),
				request.getRole() + " Logged In Successfully");
		return ResponseEntity.ok(new ApiResponse<>("success", "Login Successfully", response));
	}

//    @PostMapping("/public/send-otp") and @PostMapping("/public/verify-otp") End-point are used to check email is valid or not
	@PostMapping("/public/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {

		OtpVerificationRequest request = new OtpVerificationRequest();
		request.setEmail(email);
		request.setOtp(otp);
		boolean isValid = otpService.verifyOtp(request);

		if (isValid) {
			return ResponseEntity.ok(new ApiResponse<>("success", "Email Otp Verified", null));
		} else {
			throw new OtpException("Invalid/Expired Otp", HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/public/send-otp")
	public ResponseEntity<?> sendOtp(@RequestParam String email) {
		otpService.sendOtpToEmail(email, otpService.generateOtpAndStore(email));
		Map<String, Object> response = new HashMap<>();
		response.put("email", email);
		return ResponseEntity.ok(new ApiResponse<>("success", "Email Verification Otp Sent Successfully", response));
	}

	@PostMapping("/public/admin/login")
	public ResponseEntity<?> adminLogin(@RequestBody @Valid AdminLoginRequest request, BindingResult result) {
		if (result.hasErrors())
			throw new AdminException("Invalid details", HttpStatus.BAD_REQUEST);
		return handleLogin("ADMIN", adminService.loginWithPassword(request));
	}

	@PostMapping("/public/manager/login")
	public ResponseEntity<?> managerLogin(@RequestBody @Valid ManagerLoginRequest request, BindingResult result) {
		if (result.hasErrors())
			throw new ManagerException("Invalid details", HttpStatus.BAD_REQUEST);
		return handleLogin("MANAGER", managerService.loginWithPassword(request));
	}

	@PostMapping("/public/executive/login")
	public ResponseEntity<?> executiveLogin(@RequestBody @Valid ExecutiveLoginRequest request, BindingResult result) {
		if (result.hasErrors())
			throw new ExecutiveException("Invalid details", HttpStatus.BAD_REQUEST);
		return handleLogin("EXECUTIVE", executiveService.loginWithPassword(request));
	}

	@PostMapping("/public/trainer/login")
	public ResponseEntity<?> trainerLogin(@RequestBody @Valid TrainerLoginRequest request, BindingResult result) {
		if (result.hasErrors())
			throw new TrainerException("Invalid details", HttpStatus.BAD_REQUEST);
		return handleLogin("TRAINER", trainerService.loginWithPassword(request));
	}

	@PostMapping("/public/student/login")
	public ResponseEntity<?> studentLogin(@RequestBody @Valid StudentLoginRequest request, BindingResult result) {
		if (result.hasErrors())
			throw new StudentException("Invalid details", HttpStatus.BAD_REQUEST);
		return handleLogin("STUDENT", studentService.loginWithPassword(request));
	}

	@PostMapping("/public/studentadmin/login")
	public ResponseEntity<?> studentAdminLogin(@RequestBody @Valid StudentAdminLoginRequest request,
			BindingResult result) {
		if (result.hasErrors())
			throw new StudentAdminException("Invalid details", HttpStatus.BAD_REQUEST);
		return handleLogin("STUDENTADMIN", studentAdminService.loginWithPassword(request));
	}

	@PostMapping("/public/programhead/login")
	public ResponseEntity<?> programHeadLogin(@RequestBody @Valid ProgramheadLoginRequest request,
			BindingResult result) {
		if (result.hasErrors())
			throw new ProgramHeadException("Invalid details", HttpStatus.BAD_REQUEST);
		return handleLogin("PROGRAMHEAD", programHeadService.loginwithPassword(request));
	}

	@PostMapping("/public/hello")
	public ResponseEntity<?> forgotStu5ntPassword(@RequestParam String studentId) {

		List<StudentJdApplication> application = studentJdApplcationRepository.getUpcomingJdRound(studentId, "Rejected",
				LocalDateTime.now());

		return ResponseEntity.ok(new ApiResponse<>("success", "data", application));

	}

	@PostMapping("/public/student/forgot-password")
	public ResponseEntity<?> forgotStudentPassword(@RequestBody @Valid ForgotPasswordRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new StudentException("Invalid data", HttpStatus.BAD_REQUEST);
		}

		Student student = studentService.findByEmail(request.getEmail());
		if (student == null) {
			throw new StudentException("Account Not Found", HttpStatus.NOT_FOUND);
		}

		String passwordResetToken = idGeneration.generateRandomString();

		student.setPasswordResetToken(passwordResetToken);
		student.setPasswordTokenExpiredAt(LocalDateTime.now().plusMinutes(15));

		student = studentService.updateStudent(student);

		String passwordResetLink = FRONTEND_URL + "/auth/student/reset-password?token=" + passwordResetToken;

		String forgotPasswordHtmlTemplates = htmlTemplates.getForgotPasswordEmail(student.getName(), passwordResetLink);

		try {
			mailService.send(student.getEmail(), "Pentagon Space - Password Reset Request",
					forgotPasswordHtmlTemplates);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Password Reset Link send successfully", null));
	}

	@PostMapping("/public/user/forgot-password")
	public ResponseEntity<?> forgotUserPassword(@RequestBody @Valid UserForgotPasswordRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException("Invalid data", HttpStatus.BAD_REQUEST);
		}

		String role = request.getRole().toLowerCase();
		String passwordResetToken = idGeneration.generateRandomString();
		String resetLink = null;
		String email = request.getEmail();
		String userName = null;

		if (role.equals("trainer")) {
			Trainer trainer = trainerService.getByEmail(email);
			if (trainer == null) {
				throw new TrainerException("Email Not Found", HttpStatus.NOT_FOUND);
			}
			userName = trainer.getName();
			trainer.setPasswordResetToken(passwordResetToken);
			trainer.setPasswordTokenExpiredAt(LocalDateTime.now().plusMinutes(15));
			trainerService.updateTrainer(trainer);
		} else if (role.equals("admin")) {
			Admin admin = adminService.findByEmail(email);
			if (admin == null) {
				throw new AdminException("Email Not Founs", HttpStatus.NOT_FOUND);
			}
			userName = admin.getName();
			admin.setPasswordResetToken(passwordResetToken);
			admin.setPasswordTokenExpiredAt(LocalDateTime.now().plusMinutes(5));
			adminService.updateAdmin(admin);
		} else if (role.equals("manager")) {
			Manager manager = managerService.getManagerByEmail(email);
			if (manager == null) {
				throw new ManagerException("Email Not Found", HttpStatus.NOT_FOUND);
			}
			userName = manager.getName();
			manager.setPasswordResetToken(passwordResetToken);
			manager.setPasswordTokenExpiredAt(LocalDateTime.now().plusMinutes(15));
			managerService.updateManager(manager);
		} else if (role.equals("executive")) {
			Executive executive = executiveService.getExecutiveByEmail(email);
			if (executive == null) {
				throw new ExecutiveException("Email Not Found", HttpStatus.NOT_FOUND);
			}
			userName = executive.getName();
			executive.setPasswordResetToken(passwordResetToken);
			executive.setPasswordTokenExpiredAt(LocalDateTime.now().plusMinutes(15));
			executiveService.updateExecutive(executive);
		} else if (role.equals("studentadmin")) {
			StudentAdmin studentAdmin = studentAdminService.getByEmail(email);
			if (studentAdmin == null) {
				throw new StudentAdminException("Email Not Found", HttpStatus.NOT_FOUND);
			}
			userName = studentAdmin.getName();
			studentAdmin.setPasswordResetToken(passwordResetToken);
			studentAdmin.setPasswordTokenExpiredAt(LocalDateTime.now().plusMinutes(15));
			studentAdminService.update(studentAdmin);
		} else if (role.equals("programhead")) {
			ProgramHead programHead = programHeadService.getByEmail(email);
			if (programHead == null) {
				throw new ProgramHeadException("Email Not Found", HttpStatus.NOT_FOUND);
			}
			userName = programHead.getName();
			programHead.setPasswordResetToken(passwordResetToken);
			programHead.setPasswordTokenExpiredAt(LocalDateTime.now().plusMinutes(15));
			programHeadService.update(programHead);
		}
		resetLink = FRONTEND_URL + "/auth/reset-password?token=" + passwordResetToken + "&role=" + role;

		String forgotPasswordHtmlTemplates = htmlTemplates.getForgotPasswordEmail(userName, resetLink);

		try {
			mailService.send(request.getEmail(), "Pentagon Space - Password Reset Request",
					forgotPasswordHtmlTemplates);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Password Reset Link send successfully", null));
	}

	@PostMapping("/public/user/password-reset")
	public ResponseEntity<?> resetStudentPassword(@RequestBody @Valid UserPasswordResetRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException("Invalid data", HttpStatus.BAD_REQUEST);
		}

		String role = request.getRole().toLowerCase();
		String resetToken = request.getResetToken();
		String userName =null;
		String email=null;

		if (role.equals("trainer")) {
			Trainer trainer = trainerService.findByPasswordResetToken(resetToken);
			if (trainer == null) {
				throw new TrainerException("Invalid or expired token", HttpStatus.NOT_FOUND);
			}
			if (trainer.getPasswordTokenExpiredAt() != null
					&& LocalDateTime.now().isAfter(trainer.getPasswordTokenExpiredAt())) {
				throw new TrainerException("Reset token has expired", HttpStatus.BAD_REQUEST);
			}
			userName = trainer.getName();
			email = trainer.getEmail();
			String hashedPassword = passwordEncoder.encode(request.getNewPassword());
			trainer.setPassword(hashedPassword);
			trainer.setPasswordResetToken(null);
			trainer.setPasswordTokenExpiredAt(null);
			trainerService.updateTrainer(trainer);
		} else if (role.equals("admin")) {
			Admin admin = adminService.findByPasswordResetToken(resetToken);
			if (admin == null) {
				throw new AdminException("Invalid or expired token", HttpStatus.NOT_FOUND);
			}
			if (admin.getPasswordTokenExpiredAt() != null
					&& LocalDateTime.now().isAfter(admin.getPasswordTokenExpiredAt())) {
				throw new AdminException("Reset token has expired", HttpStatus.BAD_REQUEST);
			}
            userName = admin.getName();
            email = admin.getEmail();
			String hashedPassword = passwordEncoder.encode(request.getNewPassword());
			admin.setPassword(hashedPassword);
			admin.setPasswordResetToken(null);
			admin.setPasswordTokenExpiredAt(null);
			adminService.updateAdmin(admin);

		} else if (role.equals("manager")) {
			Manager manager = managerService.findByPasswordResetToken(resetToken);
			if (manager == null) {
				throw new ManagerException("Invalid or expired token", HttpStatus.NOT_FOUND);
			}
			if (manager.getPasswordTokenExpiredAt() != null
					&& LocalDateTime.now().isAfter(manager.getPasswordTokenExpiredAt())) {
				throw new ManagerException("Reset token has expired", HttpStatus.BAD_REQUEST);
			}
			userName = manager.getName();
			email = manager.getEmail();
			String hashedPassword = passwordEncoder.encode(request.getNewPassword());
			manager.setPassword(hashedPassword);
			manager.setPasswordResetToken(null);
			manager.setPasswordTokenExpiredAt(null);
			managerService.updateManager(manager);
		} else if (role.equals("executive")) {
			Executive executive = executiveService.findByPasswordResetToken(resetToken);
			if (executive == null) {
				throw new ExecutiveException("Invalid or expired token", HttpStatus.NOT_FOUND);
			}
			if (executive.getPasswordTokenExpiredAt() != null
					&& LocalDateTime.now().isAfter(executive.getPasswordTokenExpiredAt())) {
				throw new ExecutiveException("Reset token has expired", HttpStatus.BAD_REQUEST);
			}
			userName = executive.getName();
			email = executive.getEmail();
			String hashedPassword = passwordEncoder.encode(request.getNewPassword());
			executive.setPassword(hashedPassword);
			executive.setPasswordResetToken(null);
			executive.setPasswordTokenExpiredAt(null);
			executiveService.updateExecutive(executive);
		} else if (role.equals("studentadmin")) {
			StudentAdmin studentAdmin = studentAdminService.findByPasswordResetToken(resetToken);
			if (studentAdmin == null) {
				throw new StudentAdminException("Invalid or expired token", HttpStatus.NOT_FOUND);
			}
			if (studentAdmin.getPasswordTokenExpiredAt() != null
					&& LocalDateTime.now().isAfter(studentAdmin.getPasswordTokenExpiredAt())) {
				throw new StudentAdminException("Reset token has expired", HttpStatus.BAD_REQUEST);
			}
			userName = studentAdmin.getName();
			email = studentAdmin.getEmail();
			String hashedPassword = passwordEncoder.encode(request.getNewPassword());
			studentAdmin.setPassword(hashedPassword);
			studentAdmin.setPasswordResetToken(null);
			studentAdmin.setPasswordTokenExpiredAt(null);
			studentAdminService.update(studentAdmin);
		} else if (role.equals("programhead")) {
			ProgramHead programHead = programHeadService.findByPasswordResetToken(resetToken);
			if (programHead == null) {
				throw new ProgramHeadException("Invalid or expired token", HttpStatus.NOT_FOUND);
			}
			if (programHead.getPasswordTokenExpiredAt() != null
					&& LocalDateTime.now().isAfter(programHead.getPasswordTokenExpiredAt())) {
				throw new ProgramHeadException("Reset token has expired", HttpStatus.BAD_REQUEST);
			}
			userName = programHead.getName();
			email = programHead.getEmail();
			String hashedPassword = passwordEncoder.encode(request.getNewPassword());
			programHead.setPassword(hashedPassword);
			programHead.setPasswordResetToken(null);
			programHead.setPasswordTokenExpiredAt(null);
			programHeadService.update(programHead);
		}

		String resetSuccessTemplate = htmlTemplates.getPasswordResetSuccessEmail(userName);

		try {
			mailService.send(email, "Password Reset Successfull", resetSuccessTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Password reset successfully", null));
	}

	@PostMapping("/public/student/password-reset")
	public ResponseEntity<?> resetStudentPassword(@RequestBody @Valid PasswordResetRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new StudentException("Invalid data", HttpStatus.BAD_REQUEST);
		}

		Student student = studentService.findByPasswordResetToken(request.getResetToken());

		if (student == null) {
			throw new StudentException("Invalid or expired token", HttpStatus.NOT_FOUND);
		}

		String userName = student.getName();

		if (student.getPasswordTokenExpiredAt() != null
				&& LocalDateTime.now().isAfter(student.getPasswordTokenExpiredAt())) {
			student.setPasswordResetToken(null);
			student.setPasswordTokenExpiredAt(null);
			studentService.updateStudent(student);
			throw new StudentException("Reset token has expired", HttpStatus.BAD_REQUEST);
		}

		String hashedPassword = passwordEncoder.encode(request.getNewPassword());
		student.setPassword(hashedPassword);
		student.setPasswordResetToken(null);
		student.setPasswordTokenExpiredAt(null);

		String resetSuccessTemplate = htmlTemplates.getPasswordResetSuccessEmail(userName);

		try {
			mailService.send(student.getEmail(), "Password Reset Successfull", resetSuccessTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		studentService.updateStudent(student);

		return ResponseEntity.ok(new ApiResponse<>("success", "Password reset successfully", null));
	}

}
