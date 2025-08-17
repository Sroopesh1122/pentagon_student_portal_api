package com.pentagon.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class StudentJdApplication 
{
	@Id
	private String applicationId;
	
	@ManyToOne
	@JsonIgnore
	private JobDescription jobDescription;
	
	@ManyToOne
	@JsonIgnore
	private Student student;
	
	private LocalDateTime appliedAt;
	
	private LocalDateTime updatedAt;
	
	private String currentStatus;
	
	private String currentRound;
	
	@OneToMany(mappedBy = "studentJdApplication")
	private List<ApplicationStatusHistory> status;

}
