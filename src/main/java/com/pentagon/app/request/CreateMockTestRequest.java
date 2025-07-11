package com.pentagon.app.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMockTestRequest 
{

	@NotBlank
	private String mockName;
	
	@NotBlank
	private String trainerId;
	
	@NotBlank
	private String techId;
	
	@NotBlank
	private String batchId;
	
	@NotBlank
	private String topic;
	
	private LocalDate mockDate;
}
