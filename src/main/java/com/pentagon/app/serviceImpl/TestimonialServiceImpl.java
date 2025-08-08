package com.pentagon.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Testimonials;
import com.pentagon.app.repository.TestimonialRepository;
import com.pentagon.app.service.TestimonialService;

@Service
public class TestimonialServiceImpl implements TestimonialService {

	@Autowired
	private TestimonialRepository testimonialRepository;
	
	@Autowired
	private CloudinaryServiceImp cloudinaryService;
	
	@Override
	public Testimonials create(Testimonials testimonial) {
		return testimonialRepository.save(testimonial);
	}

	@Override
	public void delete(String testimonialId) {
		
		Testimonials testimonial = testimonialRepository.findById(testimonialId).orElse(null);
		if(testimonial !=null)
		{
			cloudinaryService.deleteFile(testimonial.getPentagonId(),"image");
			testimonialRepository.deleteById(testimonialId);
		}

	}

	@Override
	public Page<Testimonials> getTestimonil(String name, Integer passingYear, Double ctc,Pageable pageable) {
		
		return testimonialRepository.findByFilters(name, passingYear, ctc, pageable);
	}
	
	@Override
	public Testimonials finByPentagonId(String id) {
		return testimonialRepository.findByPentagonId(id);
	}

}
