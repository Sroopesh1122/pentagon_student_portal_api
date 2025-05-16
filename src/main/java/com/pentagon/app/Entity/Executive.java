package com.pentagon.app.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	
	
	@Column(name = "mobile_number", nullable = false, length = 10)
	private String mobile;
	
    @Column(nullable = false)
	private String password;
	
	@Column(name = "is_active", nullable = false)
	private boolean isAcitve;
	
	@CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
	 
	@UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
