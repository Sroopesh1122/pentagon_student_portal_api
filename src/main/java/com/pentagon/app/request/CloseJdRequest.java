package com.pentagon.app.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CloseJdRequest {

	@NotNull
	private Integer noOfClosure;
	
}
