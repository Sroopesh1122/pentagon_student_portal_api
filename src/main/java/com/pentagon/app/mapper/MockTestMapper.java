package com.pentagon.app.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.Dto.MockRatingDTO;
import com.pentagon.app.Dto.MockTestDTO;
import com.pentagon.app.entity.MockRating;
import com.pentagon.app.entity.MockTest;

import jakarta.transaction.Transactional;

@Component
public class MockTestMapper
{
	@Autowired
	private ModelMapper modelMapper;
	
	public MockTestDTO toDto(MockTest mockTest) {
	    modelMapper.typeMap(MockTest.class, MockTestDTO.class).addMappings(mapper -> {
	        mapper.map(src -> src.getBatch().getBatchId(), MockTestDTO::setBatchId);
	        mapper.map(src->src.getBatch().getName(), MockTestDTO::setBatchName);
	    });
	    return modelMapper.map(mockTest, MockTestDTO.class);
	}
	
	@Transactional
	public MockRatingDTO toDTO(MockRating mockRating)
	{
		 modelMapper.typeMap(MockRating.class, MockRatingDTO.class).addMappings(mapper->{
			mapper.map(src->src.getMockTest().getMockName(), MockRatingDTO::setMockName);
			mapper.map(src->src.getMockTest().getId(), MockRatingDTO::setTestId);
			mapper.map(src->src.getMockTest().getTrainer().getTrainerId(), MockRatingDTO::setTrainerId);
			mapper.map(src->src.getMockTest().getTrainer().getName(),MockRatingDTO::setTrainerName);
			mapper.map(src->src.getMockTest().getTechnology().getTechId(), MockRatingDTO::setTechId);
			mapper.map(src->src.getMockTest().getTechnology().getName(), MockRatingDTO::setTechName);
			mapper.map(src->src.getMockTest().getBatch().getBatchId(), MockRatingDTO::setBatchId);
			mapper.map(src->src.getMockTest().getBatch().getName(), MockRatingDTO::setBatchName);
			mapper.map(src->src.getMockTest().getTopic(), MockRatingDTO::setTopic);
		});
				
		 return modelMapper.map(mockRating, MockRatingDTO.class);
	}

}
