package com.pentagon.app.Dto;

import java.util.List;

import lombok.Data;

@Data
public class StackDTO {

	private String stackId;

    private String name;

    private List<TechnologyDTO> technologies;

	
}
