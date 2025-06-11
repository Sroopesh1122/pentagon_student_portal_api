package com.pentagon.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class ProgramHead
{
	 @Id 	
	  private String id;
	  
	  private String name;
	  
	  private String email;
	  
	  private String password;
	  
	  @ManyToMany
	  private List<Stack> stacks;

	  private String mobile;
	  
	  private String qualification;
	    
	  private Integer yearOfExperiences;
	  
	  @Column(name = "is_active", nullable = false)
	  private boolean isActive = true;
	  

	  
	  @CreationTimestamp
	    @Column(name = "created_at", updatable = false)
	    private LocalDateTime createdAt;
		
		@UpdateTimestamp
	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;

}
	

