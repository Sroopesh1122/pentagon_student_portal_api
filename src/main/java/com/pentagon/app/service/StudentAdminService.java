package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.StudentAdmin;

public interface StudentAdminService
{
  public StudentAdmin getById(String id);
  public StudentAdmin add (StudentAdmin studentAdmin);
  public StudentAdmin update(StudentAdmin studentAdmin);
  public Page<StudentAdmin> getAll(String q, Pageable pageable);
  
  public StudentAdmin getByEmail(String email);

}
