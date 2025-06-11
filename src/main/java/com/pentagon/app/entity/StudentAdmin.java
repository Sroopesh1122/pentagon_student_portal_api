package com.pentagon.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class StudentAdmin 
{
  @Id 	
  private String id;
  
  private String name;
  
  private String email;
  
  private String password;
  
  @Column(name = "is_active", nullable = false)
  private boolean isActive = true;
  
}
