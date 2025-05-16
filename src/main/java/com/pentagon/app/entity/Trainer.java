package com.pentagon.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "executive_id", nullable = false, unique = true)
    private String trainerId;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, length = 10)
	private String mobile;
	
    @Column(nullable = false)
	private String password;
	
	@Column(name = "is_active", nullable = false)
	private boolean isAcitve;
    private String trainerStack;
    private String qualification;
    private Integer yearOfExperiences;
	
	private  String   technologies ;
	
	@CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
	 
	@UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
	
	
}