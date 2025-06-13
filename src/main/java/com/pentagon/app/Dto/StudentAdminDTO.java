package com.pentagon.app.Dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StudentAdminDTO {

	private String id;

	private String name;

	private String email;

	private String password;

	private boolean isActive = true;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
