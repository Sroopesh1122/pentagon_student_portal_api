package com.pentagon.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Testimonials
{
 
	@Id
	private String testimonialId;
	
	private String pentagonId;
	
	private String studentName;
	
	@Size(max = 50000)
	private String profileImgUrl;
	
	private String profilePublicId;
	
	@Size(max = 500000)
	private String vedioLink;
	
	private Double ctc;
	
	private String role;
	
	private String collageName;
	
	private String stack;
	
	private String branch;
	
	private String  companyId;
	
	private Integer yearOfPassing;
	
	private String address;
	
	@Size(max = 100000)
	private String about;
	
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	
	
}
