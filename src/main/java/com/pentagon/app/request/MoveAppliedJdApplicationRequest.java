package com.pentagon.app.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MoveAppliedJdApplicationRequest {

	@NotBlank
	private String roundName;
	
	private List<String> applicationId;
	
	
}
