package com.pentagon.app.Dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TrainerDTO {

	private String trainerId;
    private String name;
    private String email;
    private String mobile;
    private String trainerStack;
    private String qualification;
    private Integer yearOfExperiences;
    private String technologies;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
