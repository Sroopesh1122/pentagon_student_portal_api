package com.pentagon.app.Dto;

import java.time.LocalDateTime;
import java.util.List;

import com.pentagon.app.entity.Technology;

import lombok.Data;

@Data
public class TrainerDTO {

	private String trainerId;
    private String name;
    private String email;
    private String mobile;
//  private String trainerStack;
    private List<Technology> technologies;
    private String qualification;
    private Integer yearOfExperiences;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String programHeadId;
    
    private List<BatchTechTrainerDTO> batchTechTrainer;
}
