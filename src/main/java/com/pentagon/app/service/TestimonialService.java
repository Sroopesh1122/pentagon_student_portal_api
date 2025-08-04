package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Testimonials;

public interface TestimonialService
{
  public Testimonials create(Testimonials testimonial);
  public void delete(String testimonialId);
  public Page<Testimonials> getTestimonil(String name,Integer passingYear,Double ctc,Pageable pageable);
  
  public Testimonials finByPentagonId(String id);
  
}
