package com.pentagon.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.Dto.BatchDTO;
import com.pentagon.app.entity.Batch;

@Component
public class BatchMapper {

	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public BatchDTO toDTO(Batch batch)
	{
		return modelMapper.map(batch, BatchDTO.class);
	}
	
}
