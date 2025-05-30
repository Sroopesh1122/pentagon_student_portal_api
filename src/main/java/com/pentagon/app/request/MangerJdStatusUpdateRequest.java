package com.pentagon.app.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MangerJdStatusUpdateRequest 
{
	@NotNull
	private String jdId;
	@NotNull
	private String status;
	
	private String actionReason;

}
