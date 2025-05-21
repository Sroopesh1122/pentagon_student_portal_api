package com.pentagon.app.requestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerificationRequest {
	
	@NotBlank
	private String role;

	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String otp;
}
