package com.pentagon.app.Dto;

import java.util.List;

import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.ProgramHead;
import com.pentagon.app.entity.Technology;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class StackDTO {

	private String stackId;

    private String name;

    private List<TechnologyDTO> technologies;

	
}
