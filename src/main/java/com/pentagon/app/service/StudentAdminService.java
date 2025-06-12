package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.StudentAdmin;
import com.pentagon.app.request.StudentAdminLoginRequest;

import jakarta.validation.Valid;

public interface StudentAdminService
{
  public StudentAdmin getById(String id);
  public StudentAdmin add (StudentAdmin studentAdmin);
  public StudentAdmin update(StudentAdmin studentAdmin);
  public Page<StudentAdmin> getAll(String q, Pageable pageable);
  
  public StudentAdmin getByEmail(String email);
  public String loginWithPassword(@Valid StudentAdminLoginRequest request);

}
