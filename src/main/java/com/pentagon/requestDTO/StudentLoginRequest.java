package com.pentagon.requestDTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class StudentLoginRequest {

	@Column(nullable = false)
	private String studentId;
	@Column(nullable = false)
	private String password;
}
