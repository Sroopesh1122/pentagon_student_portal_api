package com.pentagon.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="job_description")
public class JobDescription {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
    @Column(name = "job_description_id", unique = true)
	private String jobDescriptionId;
	
    @Column(name = "company_name", nullable = false)
	private String companyName;
	
    @Column(name = "website", nullable = false)
	private String website;
	
    @Column(name="description",nullable = false)
    @Size(max = 50000) 
    private String Description;
    
    @Column(nullable = false)
	private String qualification;
	
    @Column(nullable = false)
	private String stream;
    
    @Column(nullable = false)
	private Double percentage;
	
	@Column(name = "min_year_of_passing")
	private Integer minYearOfPassing;
	
	@Column(name = "max_year_of_passing")
	private Integer maxYearOfPassing;
	
	@Column(name="stack")
	private String stack;
	
	@Column(name="package",nullable = false)
	private String salaryPackage;
	
	@Column(name = "no_of_registrations")
	private Integer numberOfRegistrations=0;
	
	@Column(name = "current_registrations")
	private Integer currentRegistrations;
	
	@Column(name = "mock_rating")
	private Double mockRating;
	
	@Column(name = "JD_status", nullable = false)
	private boolean jdStatus = false;
	
	@Column(name = "manager_approval", nullable = false)
	private boolean managerApproval;
	
	@Column(name = "number_of_closures")
	private Integer numberOfClosures=0;
	
	@Column(name="jd_closed")
	private boolean isClosed = false;
	
	@CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
	 
	@UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
	
    // Business method to close the JD
    public void closeJobDescription() {
        this.jdStatus = true;
        this.updatedAt = LocalDateTime.now();
    }

    // Business method to increment registrations
//    public void incrementRegistrations() {
//        this.numberOfRegistrations = (this.numberOfRegistrations == null) ? 1 : this.numberOfRegistrations + 1;
//    }
	
}