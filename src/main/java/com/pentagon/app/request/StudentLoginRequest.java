package com.pentagon.app.request;

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
