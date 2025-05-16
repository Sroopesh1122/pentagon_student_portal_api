package com.pentagon.requestDTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ExecutiveLoginRequest {

	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String otp;
}
