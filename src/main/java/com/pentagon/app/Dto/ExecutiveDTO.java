package com.pentagon.app.Dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExecutiveDTO {

	 private Integer Id;
		private String executiveId;
		private String name;
		private String email;
		private String mobile;
		private boolean active;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
}
