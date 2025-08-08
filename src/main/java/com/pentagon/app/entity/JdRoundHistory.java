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
public class JdRoundHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long historyId;
	
	@ManyToOne
	@JsonIgnore
	private JobDescription jobDescription;
	
	private String round;
	
	private LocalDateTime scheduleDate;
	
	private String desciption;
	
	private LocalDateTime createdAt;
}
