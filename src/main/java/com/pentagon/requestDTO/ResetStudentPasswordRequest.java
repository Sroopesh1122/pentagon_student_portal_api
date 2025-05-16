package com.pentagon.requestDTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ResetStudentPasswordRequest {
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String confirmPassword;
}
