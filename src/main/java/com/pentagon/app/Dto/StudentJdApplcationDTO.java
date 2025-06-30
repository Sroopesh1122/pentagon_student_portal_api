package com.pentagon.app.Dto;

import java.time.LocalDateTime;
import java.util.List;

import com.pentagon.app.entity.ApplicationStatusHistory;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Student;

import lombok.Data;

@Data
public class StudentJdApplcationDTO {

    private String applicationId;
	
	private JobDescription jobDescription;
	
	private Student student;
	
	private LocalDateTime appliedAt;
	
	private LocalDateTime updatedAt;
	
	private String currentStatus;
	
	private String currentRound;
	
	private List<ApplicationStatusHistory> status;
	
}
