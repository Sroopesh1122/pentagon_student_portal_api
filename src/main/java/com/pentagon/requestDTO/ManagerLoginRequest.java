package com.pentagon.requestDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ManagerLoginRequest {
	@NotBlank
	 @Email(message = "Invalid email format")
	private String email;
	@NotBlank
	private String otp;
}
