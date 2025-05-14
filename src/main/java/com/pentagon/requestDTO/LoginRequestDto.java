package com.pentagon.requestDTO;

import lombok.Data;

@Data
public class LoginRequestDto {
	
	private String email;
	private String password;
	private String otp;

}
