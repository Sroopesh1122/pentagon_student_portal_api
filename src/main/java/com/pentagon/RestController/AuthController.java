package com.pentagon.RestController;

import org.springframework.web.bind.annotation.RestController;

import com.pentagon.requestDTO.LoginRequestDto;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/penatagon/api")
public class AuthController {
	@PostMapping("/auth/login")
	public ResponseEntity<?> Signin(@RequestBody LoginRequestDto loginRequestDto) {
		if (loginRequestDto.getPassword()!= null) {
			//login logic for email with password
		}else if (loginRequestDto.getOtp()!=null) {
			// login login for email with otp
		}else {
			throw new RuntimeException("Password or OTP Required");
		}
		return null; 
	}
	
	@PostMapping("")
	public String postMethodName(@RequestBody String entity) {
		//TODO: process POST request
		
		return entity;
	}
	
	
	
	
	
}
