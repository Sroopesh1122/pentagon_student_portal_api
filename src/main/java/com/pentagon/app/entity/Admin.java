package com.pentagon.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "admin")
public class Admin {
	@Id
	@Column(name = "admin_id", nullable = false, unique = true)
	private String adminId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, length = 10)
	private String mobile;

	@JsonIgnore
	@Column(nullable = false)
	private String password;

	@JsonIgnore
	private String passwordResetToken;

	@JsonIgnore
	private LocalDateTime passwordTokenExpiredAt;
	
	
	private String profileImgUrl = "https://cdn-icons-png.flaticon.com/512/3685/3685367.png";
	
	@JsonIgnore
	private String profilePublicId;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}