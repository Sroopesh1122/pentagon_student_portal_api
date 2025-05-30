package com.pentagon.app.Dto;

import java.time.LocalDateTime;
import java.util.List;

import com.pentagon.app.entity.JobDescription;

import lombok.Data;

@Data
public class ExecutiveDTO {

	 private Integer Id;
		private String executiveId;
		private String name;
		private String email;
		private String mobile;
		private String password;
		private boolean active;
		private String managerId;
		private List<JobDescription> jobDescription;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
}
