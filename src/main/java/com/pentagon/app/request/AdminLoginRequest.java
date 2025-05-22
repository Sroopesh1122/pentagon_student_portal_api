package com.pentagon.app.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminLoginRequest {
	@NotBlank
	 @Email(message = "Invalid email format")
	private String email;
	@NotBlank
	private String password;

}
