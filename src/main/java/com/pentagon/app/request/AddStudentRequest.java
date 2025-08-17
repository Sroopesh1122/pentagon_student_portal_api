package com.pentagon.app.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddStudentRequest {
	@NotBlank
	private String name;
	@NotBlank
	@Email(message = "Invalid email format")
	private String email;
	@NotBlank
	@Size(min=10,max=10)
	private String mobile;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String stack;
	@NotBlank
	private String typeOfAdmission;
	@NotBlank
	private String  mode; // online or offline
}
