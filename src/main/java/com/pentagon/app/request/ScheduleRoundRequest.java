package com.pentagon.app.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ScheduleRoundRequest {

	private String roundName;
	
	private String date;
	
	private String jdId;
	
}
