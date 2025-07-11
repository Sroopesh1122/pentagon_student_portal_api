package com.pentagon.app.request;

import lombok.Data;

@Data
public class UpdateMockRatingRequest 
{

	private String mockRatingId;
	
	private Double rating;
	
	private boolean attendance;
	
	private String remark;
}
