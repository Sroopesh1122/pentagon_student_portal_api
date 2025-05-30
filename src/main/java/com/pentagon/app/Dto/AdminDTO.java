package com.pentagon.app.Dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminDTO {

	private Integer Id;
	private String adminId;
	private String name;
	private String email;
	private String mobile;
	private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
