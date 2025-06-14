package com.pentagon.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.Dto.StudentAdminDTO;
import com.pentagon.app.entity.StudentAdmin;

@Component
public class StudentAdminMapper {

	
	@Autowired
	private ModelMapper modelMapper;
	
	public StudentAdminDTO toDto(StudentAdmin studentAdmin)
	{
		return modelMapper.map(studentAdmin, StudentAdminDTO.class);
	}
	
}
