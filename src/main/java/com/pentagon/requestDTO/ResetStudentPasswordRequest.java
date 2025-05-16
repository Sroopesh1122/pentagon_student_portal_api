package com.pentagon.requestDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetStudentPasswordRequest {
	@NotBlank
	private String password;
	@NotBlank
	private String confirmPassword;
}
