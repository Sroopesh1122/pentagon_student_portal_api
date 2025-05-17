package com.pentagon.app.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddTrainerRequest {

	@NotBlank
	private String name;
	@NotBlank
	private String email;
	@NotBlank
	private String mobile;
	@NotBlank
	private String password;
	@NotBlank
	private String trainerStack;
	@NotBlank
	private String qualification;
	@NotNull
	private Integer yearOfExperiences;
	@NotBlank	
	private  String technologies ;
}
