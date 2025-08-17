package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Enquiry;
import com.pentagon.app.repository.EnquiryRepository;
import com.pentagon.app.service.EnquiryService;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private EnquiryRepository enquiryRepository;
	
	@Override
	public Enquiry create(Enquiry enquiry) {
		enquiry.setCreatedAt(LocalDateTime.now());
		return enquiryRepository.save(enquiry);
	}

	@Override
	public Enquiry update(Enquiry enquiry) {
		enquiry.setUpdatedAt(LocalDateTime.now());
		return  enquiryRepository.save(enquiry);
	}

	@Override
	public Page<Enquiry> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return enquiryRepository.findAll(pageable);
	}

}
