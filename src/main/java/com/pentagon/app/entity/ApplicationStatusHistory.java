package com.pentagon.app.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ApplicationStatusHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long applicationHistoryId;
	
	private String status;
	
	private String round;
	
	private String remarks;
	
	@ManyToOne
	@JsonIgnore
	private StudentJdApplication studentJdApplication;
	
	private LocalDateTime createdAt;
}
