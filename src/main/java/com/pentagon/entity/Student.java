package com.pentagon.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="student")
public class Student {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	@Column(name = "student_id", nullable = false, unique = true)
	private String studentId;
	
	@Column(nullable = false)
	private String name;
	
	private String gender;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, length = 10)
	private String mobile;
	
	@Column(nullable = false)
    private String stack;
	
	private String address;
	
	private String college;
	
	private String qualification;
	
	private Integer yearOfPassout;
	
	private Double perecentage;
	
	private String stream;
	
	@Size(max = 50000)
    private String objective;
	
    @Size(max = 50000)
	private String skills;
	    
	@Size(max = 50000)
	private String internship;
	
    @Size(max = 50000)
	private String projects;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.PENDING;
    
    public enum EnrollmentStatus { 
    	PENDING,
        ACTIVE, 
        SUSPENDED, 
        COMPLETED, 
        DROPPED,
        PLACED
    }
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
	 
	@UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}