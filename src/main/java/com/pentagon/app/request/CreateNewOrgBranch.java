package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNewOrgBranch {

	@NotBlank
	private String branchName;

	@NotBlank
	private String branchAddress;
	
	private Boolean isHeadOffice =false;

}
