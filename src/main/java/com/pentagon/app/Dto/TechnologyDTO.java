package com.pentagon.app.Dto;

import java.util.List;

import lombok.Data;

@Data
public class TechnologyDTO {

	private String techId;
	   
    private String name;
    
    List<TrainerDTO> trainers;
}
