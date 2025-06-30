package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JdApplicationApplyRequest {
	@NotBlank
	private String studentId;
	@NotBlank
	private String jdId;
	
}
