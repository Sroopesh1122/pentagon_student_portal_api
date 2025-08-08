package com.pentagon.app.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBatchRequest {

	private String batchName;
	
	@NotBlank
	private String batchMode;
	
	@NotBlank
	private String stackId;
	
//	private List<ScheduleDetails> scheduleDetails;
	
}


