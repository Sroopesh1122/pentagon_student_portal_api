package com.pentagon.app.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pentagon.app.entity.Student;

import lombok.Data;

@Data
public class MockRatingDTO 
{
	private String id;

    private Student student;

    private Double rating =0.0;

    private String remark;

    private boolean attendance = false;
       
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private String mockName;
    
    private String testId;

    private String topic;

    private LocalDate mockDate;
    
    private String trainerId;
    
    private String trainerName;
    
    private String batchId;
    
    private String batchName;
    
    private String techId;
    
    private String techName;
    
    
}
