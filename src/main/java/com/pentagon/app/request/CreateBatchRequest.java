package com.pentagon.app.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBatchRequest {

	private String batchName;
	
	@NotBlank
	private String batchMode;
	
	@NotBlank
	private String stackId;
	
	private LocalDate startDate;
	
	
}


