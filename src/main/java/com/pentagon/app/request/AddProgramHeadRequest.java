package com.pentagon.app.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddProgramHeadRequest {
	@NotBlank
	private String name;
	@NotBlank
	@Email(message = "Invalid email format")
	private String email;
	@Size(min = 10, max = 10)
	@NotBlank
	private String mobile;

	@Size(min = 1, message = "At least one stack ID must be provided")
	private List<String> stackIds;
	
	@Size(min = 1, message = "At least one technologies ID must be provided")
	private List<String> technologies;
}
