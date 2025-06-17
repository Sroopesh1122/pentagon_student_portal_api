package com.pentagon.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.entity.JobDescription;

@Component
public class JobDescriptionMapper {

	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public JobDescriptionDTO toDTO(JobDescription jobDescription)
	{
		return modelMapper.map(jobDescription, JobDescriptionDTO.class);
	}
	
}
