package com.pentagon.app.Dto;

import java.time.LocalDateTime;
import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.entity.Trainer;
import lombok.Data;

@Data
public class BatchTechTrainerDTO {

	private Long id;

    private Batch batch;

    private Technology technology;

    private Trainer trainer;
    
    private LocalDateTime createdAt;
}
