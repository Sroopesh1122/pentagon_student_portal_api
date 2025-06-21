package com.pentagon.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.Dto.BatchTechTrainerDTO;
import com.pentagon.app.entity.BatchTechTrainer;

@Component
public class BatchTechTrainerMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public BatchTechTrainerDTO toDTO(BatchTechTrainer batchTechTrainer)
	{
		return modelMapper.map(batchTechTrainer, BatchTechTrainerDTO.class);
	}

}
