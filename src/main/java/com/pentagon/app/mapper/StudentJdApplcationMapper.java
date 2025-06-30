package com.pentagon.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.Dto.StudentJdApplcationDTO;
import com.pentagon.app.entity.StudentJdApplication;

@Component
public class StudentJdApplcationMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	
	public StudentJdApplcationDTO toDto(StudentJdApplication studentJdApplication)
	{
		return modelMapper.map(studentJdApplication, StudentJdApplcationDTO.class);
	}

}
