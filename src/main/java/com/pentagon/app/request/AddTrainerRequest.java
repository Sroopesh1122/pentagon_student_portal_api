package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTrainerRequest {

	@NotBlank
	private String name;
	@NotBlank
	private String email;
	@NotBlank
	private String mobile;
	@NotBlank
	private String trainerStack;
	@NotBlank
	private String qualification;
	@NotNull
	private Integer yearOfExperiences;
	@NotBlank	
	private  String technologies ;
}
