package com.pentagon.app.request;

import lombok.Data;

@Data
public class ScheduleDetails {

	private String techId;
	private Double startTime;
	private Double endTime;
	private String trainerId;
}
