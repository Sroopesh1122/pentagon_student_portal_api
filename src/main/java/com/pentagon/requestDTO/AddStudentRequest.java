package com.pentagon.requestDTO;

import lombok.Data;

@Data
public class AddStudentRequest {

	private String name;
	private String email;
	private String mobile;
	private String stack;
}
