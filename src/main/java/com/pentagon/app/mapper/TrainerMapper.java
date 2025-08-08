package com.pentagon.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.Trainer;

import jakarta.transaction.Transactional;

@Component
public class TrainerMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	@Transactional
	public TrainerDTO toDTO(Trainer trainer)
	{
		return modelMapper.map(trainer, TrainerDTO.class);
	}
}
