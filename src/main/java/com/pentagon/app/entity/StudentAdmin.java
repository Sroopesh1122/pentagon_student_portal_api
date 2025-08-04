package com.pentagon.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class StudentAdmin {
	@Id
	private String id;

	private String name;

	private String email;

	@JsonIgnore
	private String password;
	
	private String mobile;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;
	
	
	@JsonIgnore
	private String passwordResetToken;

	@JsonIgnore
	private LocalDateTime passwordTokenExpiredAt;
	
	
	private String profileImgUrl = "https://www.shutterstock.com/image-vector/default-avatar-profile-icon-social-600nw-1677509740.jpg";

	@JsonIgnore
	private String profileImgPublicId;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
