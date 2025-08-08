package com.pentagon.app.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateManagerRequest {

	@NotBlank
	private String name;
	
	@Email(message = "Invalid email format")
	@NotBlank
	private String email;
	
	@Size(min=10,max=10)
	@NotBlank
	private String mobile;
	
	@NotBlank
	private String password;
	
	
}