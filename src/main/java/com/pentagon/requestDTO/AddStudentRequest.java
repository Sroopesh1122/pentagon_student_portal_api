package com.pentagon.requestDTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AddStudentRequest {
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false, length=10)
	private String mobile;
	@Column(nullable = false)
	private String stack;
	@Column(nullable = false)
	private String typeOfAdmission;
}
