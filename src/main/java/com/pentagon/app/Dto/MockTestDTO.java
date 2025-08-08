package com.pentagon.app.Dto;

import java.time.LocalDate;


import lombok.Data;

@Data
public class MockTestDTO 
{

    private Long id;

    private String batchId;
    private String batchName;
    private String technologyName;
    private String techId;
    private String trainerId;
    private String trainerName;
    private String mockName;
    private String topic;
    private LocalDate mockDate;
}
