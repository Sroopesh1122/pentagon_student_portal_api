package com.pentagon.app.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddExecutiveRequest {

	@NotBlank
	private String name;
	@NotBlank
	 @Email(message = "Invalid email format")
	private String email;
	@Size(min=10,max=10)
	@NotBlank
	private String mobile;
	
	
	//It is  optional since it is used in both admin and executive controller
	private String managerId;
	
}
