package com.pentagon.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.Dto.StudentDTO;
import com.pentagon.app.entity.Student;

@Component
public class StudentMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	
	public StudentDTO toDto(Student student)
	{
		return modelMapper.map(student, StudentDTO.class);
	}
	
	
}
