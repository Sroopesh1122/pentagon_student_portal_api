package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetStudentPasswordRequest {
	@NotBlank
	private String password;
	@NotBlank
	private String confirmPassword;
}
