package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateJobDescriptionRequest {
   
	@NotBlank
	private String jobDescriptionId;

	private Integer noOfRegistration;
}
