package com.pentagon.app.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddTrainerRequest {

	@NotBlank
	private String name;
	@NotBlank
	private String email;
	@NotBlank
	private String mobile;
	@Size(min = 1, message = "At least one technology ID must be provided")
	private List<String> techId;
	
}
