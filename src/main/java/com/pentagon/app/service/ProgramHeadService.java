package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.ProgramHead;

public interface ProgramHeadService 
{
  public ProgramHead getById(String id);
  public ProgramHead add(ProgramHead programHead);
  public ProgramHead update(ProgramHead programHead);
  public Page<ProgramHead> getAll(Pageable pageable);
  public ProgramHead getByEmail(String email);
}
