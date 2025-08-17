package com.pentagon.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class OrganizationBranch {

	@Id
	private String id;
	
	private String branchName;
	
	@Size(max = 10000)
	private String branchAddress;
	
	private boolean isHeadOffice= false;
	
	
	private Boolean isActive= true;
	

	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
}
