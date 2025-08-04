package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateExecutiveManager {

	@NotBlank
	private String managerId;
}
