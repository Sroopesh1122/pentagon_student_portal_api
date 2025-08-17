package com.pentagon.app.Dto;

import java.time.LocalDateTime;
import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.Technology;
import lombok.Data;

@Data
public class BatchTechTrainerDTO {

	private Long id;

    private Batch batch;

    private Technology technology;

    private TrainerDTO trainers;
    
    private LocalDateTime createdAt;
    
    private Double startTime;
    
    private Double endTime;
}
