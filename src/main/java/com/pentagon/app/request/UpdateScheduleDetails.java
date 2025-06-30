package com.pentagon.app.request;

import lombok.Data;

@Data
public class UpdateScheduleDetails {

	private Integer id;
	private String techId;
	private Double startTime;
	private Double endTime;
	private String trainerId;
}
