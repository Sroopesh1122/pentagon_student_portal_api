package com.pentagon.app.restController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.pentagon.app.exception.*;
import com.pentagon.app.requestDTO.*;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.*;
import com.pentagon.app.utils.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pentagon/auth")
public class AuthController {

    private final AdminService adminService;
    private final ExecutiveService executiveService;
    private final ManagerService managerService;
    private final StudentService studentService;
    private final TrainerService trainerService;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;

    public AuthController(AdminService adminService, ManagerService managerService, ExecutiveService executiveService,
                          TrainerService trainerService, StudentService studentService,OtpService otpService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.managerService = managerService;
        this.executiveService = executiveService;
        this.trainerService = trainerService;
        this.studentService = studentService;
        this.otpService =otpService;
        this.jwtUtil = jwtUtil;
    }

    private ResponseEntity<?> handleLogin(String role, String result) {
        return ResponseEntity.ok(new ApiResponse<>("success", result, null));
    }

    @PostMapping("/secure/verify-OTP")
    private ResponseEntity<?> handleOtpVerification(OtpVerificationRequest request) {
    	otpService.verifyOtp(request);
//        if (!isVerified) {
//            throw switch (role.toUpperCase()) {
//                case "ADMIN" -> new AdminException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
//                case "MANAGER" -> new ManagerException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
//                case "EXECUTIVE" -> new ExecutiveException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
//                case "TRAINER" -> new TrainerException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
//                case "STUDENT" -> new StudentException("OTP is Invalid/Expired", HttpStatus.UNAUTHORIZED);
//                default -> new RuntimeException("Unknown role");
//            };
//        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", request.getEmail());
        claims.put("role", request.getRole());

        String token = jwtUtil.generateToken(request.getEmail(), claims);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("token", token);
        response.put("message", "LOGIN SUCCESSFULLY");

        return ResponseEntity.ok(new ApiResponse<>("success", "Login Successfully", response));
    }

    @PostMapping("/public/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody @Valid AdminLoginRequest request, BindingResult result) {
        if (result.hasErrors()) throw new AdminException("Invalid details", HttpStatus.BAD_REQUEST);
        return handleLogin("ADMIN", adminService.loginWithPassword(request));
    }
    
    @PostMapping("/public/manager/login")
    public ResponseEntity<?> managerLogin(@RequestBody @Valid ManagerLoginRequest request, BindingResult result) {
        if (result.hasErrors()) throw new ManagerException("Invalid details", HttpStatus.BAD_REQUEST);
        return handleLogin("MANAGER", managerService.loginWithPassword(request));
    }

    @PostMapping("/public/executive/login")
    public ResponseEntity<?> executiveLogin(@RequestBody @Valid ExecutiveLoginRequest request, BindingResult result) {
        if (result.hasErrors()) throw new ExecutiveException("Invalid details", HttpStatus.BAD_REQUEST);
        return handleLogin("EXECUTIVE", executiveService.loginWithPassword(request));
    }

    @PostMapping("/public/trainer/login")
    public ResponseEntity<?> trainerLogin(@RequestBody @Valid TrainerLoginRequest request, BindingResult result) {
        if (result.hasErrors()) throw new TrainerException("Invalid details", HttpStatus.BAD_REQUEST);
        return handleLogin("TRAINER", trainerService.loginWithPassword(request));
    }

    @PostMapping("/public/student/login")
    public ResponseEntity<?> studentLogin(@RequestBody @Valid StudentLoginRequest request, BindingResult result) {
        if (result.hasErrors()) throw new StudentException("Invalid details", HttpStatus.BAD_REQUEST);
        return handleLogin("STUDENT", studentService.loginWithPassword(request));
    }
    
//    @PostMapping("/admin/verify-OTP")
//    public ResponseEntity<?> adminVerify(@RequestBody @Valid OtpVerificationRequest request, BindingResult result) {
//        if (result.hasErrors()) throw new AdminException("Invalid details", HttpStatus.BAD_REQUEST);
//        return handleOtpVerification("ADMIN", request, adminService.verifyOtp(request));
//    }
//    @PostMapping("/manager/verify-OTP")
//    public ResponseEntity<?> managerVerify(@RequestBody @Valid OtpVerificationRequest request, BindingResult result) {
//        if (result.hasErrors()) throw new ManagerException("Invalid details", HttpStatus.BAD_REQUEST);
//        return handleOtpVerification("MANAGER", request, managerService.verifyOtp(request));
//    }
//    @PostMapping("/executive/verify-OTP")
//    public ResponseEntity<?> executiveVerify(@RequestBody @Valid OtpVerificationRequest request, BindingResult result) {
//        if (result.hasErrors()) throw new ExecutiveException("Invalid details", HttpStatus.BAD_REQUEST);
//        return handleOtpVerification("EXECUTIVE", request, executiveService.verifyOtp(request));
//    }
//    @PostMapping("/trainer/verify-OTP")
//    public ResponseEntity<?> trainerVerify(@RequestBody @Valid OtpVerificationRequest request, BindingResult result) {
//        if (result.hasErrors()) throw new TrainerException("Invalid details", HttpStatus.BAD_REQUEST);
//        return handleOtpVerification("TRAINER", request, trainerService.verifyOtp(request));
//    }
//    @PostMapping("/student/verify-OTP")
//    public ResponseEntity<?> studentVerify(@RequestBody @Valid OtpVerificationRequest request, BindingResult result) {
//        if (result.hasErrors()) throw new StudentException("Invalid details", HttpStatus.BAD_REQUEST);
//        return handleOtpVerification("STUDENT", request, studentService.verifyOtp(request));
//    }
}
