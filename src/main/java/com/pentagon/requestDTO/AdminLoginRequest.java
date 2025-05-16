package com.pentagon.requestDTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AdminLoginRequest {
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;

}
