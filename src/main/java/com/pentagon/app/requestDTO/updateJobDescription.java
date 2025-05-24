package com.pentagon.app.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class updateJobDescription {

	@NotBlank
	private String JobId;
	@NotBlank
	private String executiveId;
	@NotNull
	@PositiveOrZero
	private Integer numberOfClosures;
}
