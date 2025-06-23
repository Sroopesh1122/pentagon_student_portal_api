package com.pentagon.app.Dto;

import java.time.LocalDateTime;

import com.pentagon.app.entity.Stack;

import lombok.Data;

@Data
public class BatchDTO {
	private String batchId;

	private String name;

	private String mode;

	private Stack stack;
	
	private LocalDateTime createdAt;
}
