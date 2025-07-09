package com.pentagon.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Announcement
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Size(max = 50000)
	private String description;

	@Size(max = 50000)
	private String link;

	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "batch_announcements",
			joinColumns = @JoinColumn(name="announcementId"),
			inverseJoinColumns = @JoinColumn(name="batchId")
			)
	
	private List<Batch> batches;

	
	private LocalDateTime  createdAt;
	
	private String employeeId;

}
