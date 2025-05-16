package com.pentagon.app.requestDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentLoginRequest {

	@NotBlank
	private String studentId;
	@NotBlank
	private String password;
}
