package com.pentagon.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data

@Entity
@Table(name = "executive")
public class Executive {

	@Id
	@Column(name = "executive_id", nullable = false, unique = true)
	private String executiveId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, length = 10)
	private String mobile;

	@JsonIgnore
	@Column(nullable = false)
	private String password;

	@Column(name = "is_active")
	private boolean active; // for blocking

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	private String managerId;

	@JsonIgnore
	private String passwordResetToken;

	@JsonIgnore
	private LocalDateTime passwordTokenExpiredAt;

	private String profileImgUrl = "https://www.shutterstock.com/image-vector/default-avatar-profile-icon-social-600nw-1677509740.jpg";

	@JsonIgnore
	private String profileImgPublicId;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "executive", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<JobDescription> jobDescription;

}