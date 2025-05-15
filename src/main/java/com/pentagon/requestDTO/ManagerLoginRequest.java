package com.pentagon.requestDTO;

import lombok.Data;

@Data
public class ManagerLoginRequest {

	private String email;
	private String otp;
}
