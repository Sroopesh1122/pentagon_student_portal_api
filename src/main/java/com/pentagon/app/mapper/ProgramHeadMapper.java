package com.pentagon.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.Dto.ProgramHeadDTO;
import com.pentagon.app.entity.ProgramHead;

@Component
public class ProgramHeadMapper {

	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public ProgramHeadDTO toDTO(ProgramHead programHead)
	{
		return modelMapper.map(programHead, ProgramHeadDTO.class);
	}
	
}
