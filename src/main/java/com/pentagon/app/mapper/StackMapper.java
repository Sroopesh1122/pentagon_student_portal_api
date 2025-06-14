package com.pentagon.app.mapper;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.Dto.StackDTO;
import com.pentagon.app.entity.Stack;

@Component
public class StackMapper
{
    
	@Autowired
	private ModelMapper modelMapper;
	
	public StackDTO convertToDTO(Stack stack)
	{
		return modelMapper.map(stack, StackDTO.class);
	}

	public Stack convertToEntity(StackDTO stackDTO)
	{
		return modelMapper.map(stackDTO, Stack.class);
	}
}
