package com.pentagon.app.requestDTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TrainerDTO {

	private Integer id;
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
