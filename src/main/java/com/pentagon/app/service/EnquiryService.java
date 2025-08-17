package com.pentagon.app.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Enquiry;

public interface EnquiryService
{
  public Enquiry create(Enquiry enquiry);
  public Enquiry update(Enquiry enquiry);
  public Page<Enquiry> findAll(Pageable  pageable);
}
