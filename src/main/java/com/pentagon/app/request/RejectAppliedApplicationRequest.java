package com.pentagon.app.request;

import java.util.List;

import lombok.Data;

@Data
public class RejectAppliedApplicationRequest {

	private List<String> applicationId;
	
	private String round;
}
