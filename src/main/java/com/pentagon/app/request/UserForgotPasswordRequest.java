package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserForgotPasswordRequest {
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String role;

}
