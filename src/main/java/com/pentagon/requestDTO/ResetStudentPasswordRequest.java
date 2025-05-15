package com.pentagon.requestDTO;

import lombok.Data;

@Data
public class ResetStudentPasswordRequest {

	String password;
	String confirmPassword;
}
