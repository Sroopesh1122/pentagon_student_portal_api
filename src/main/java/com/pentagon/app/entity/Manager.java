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
@Table(name = "manager")
public class Manager {

	@Id
	@Column(name = "manager_id", nullable = false, unique = true)
	private String managerId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, length = 10)
	private String mobile;

	@Column(nullable = false)
	private String password;

	@Column(name = "is_active", nullable = false)
	private boolean active;

	@JsonIgnore
	private String passwordResetToken;

	@JsonIgnore
	private LocalDateTime passwordTokenExpiredAt;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}