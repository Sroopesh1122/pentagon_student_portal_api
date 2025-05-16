package com.pentagon.requestDTO;

import jakarta.persistence.Column;
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
	
}
