package com.pentagon.app.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerificationRequest {

	@NotBlank(message = "Role is required")
	@NotBlank
	private String role;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "OTP is required")
	private String otp;
}
