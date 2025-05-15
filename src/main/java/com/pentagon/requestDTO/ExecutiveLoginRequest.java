package com.pentagon.requestDTO;

import lombok.Data;

@Data
public class ExecutiveLoginRequest {

	private String email;
	private String otp;
}
