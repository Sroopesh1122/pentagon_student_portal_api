package com.pentagon.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data

@Entity
@Table(name="executive")
public class Executive {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	@Column(name = "executive_id", nullable = false, unique = true)
	private String executiveId;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, length = 10)
	private String mobile;
	
    @Column(nullable = false)
	private String password;
	
	@Column(name = "is_active")
	private boolean  active;
	
	@CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
	
	
	private String managerId;
	 
	@UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
	@OneToMany(mappedBy = "executive", cascade = CascadeType.ALL)
	@JsonIgnore
    private List<JobDescription> jobDescription;
	
	
}