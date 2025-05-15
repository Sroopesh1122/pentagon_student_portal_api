package com.pentagon.requestDTO;

import lombok.Data;

@Data
public class StudentLoginRequest {

	private String studentId;
	private String password;
}
