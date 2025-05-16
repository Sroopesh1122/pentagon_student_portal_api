package com.pentagon.Entity;

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
@Table(name="job_description")
public class JobDescription {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
    @Column(name = "job_description_id", unique = true, length = 20)
	private String jobDescriptionId;
	
    @Column(name = "company_name", nullable = false)
	private String companyName;
	
    @Column(name = "company_name", nullable = false)
	private String website;
	
    @Column(nullable = false)
	private String qualification;
	
    @Column(nullable = false)
	private String stream;
    
	private Double percentage;
	
	@Column(name = "year_of_passing")
	private Integer year_of_passing;
	
	@Column(name="stack")
	private String stack;
	
	@Column(nullable = false)
	private String skills;
	
	@Column(nullable = false)
	private String salary;
	 
	private String bond;
	
	@Column(name = "job_location", nullable = false)
	private String jobLocation;
	
	@Column(name = "interview_mode", nullable = false)
	private String interviewMode;
	
	@Column(name = "interview_rounds", nullable = false)
	private String interviewRounds;
	
	@Column(name = "no_of_registrations")
	private Integer no_of_registrations=0;
	
	@Column(name = "mock_rating")
	private Integer mockRating;
	
	@Column(name = "JD_status", nullable = false)
	private boolean JD_status = false;
	
	@Column(name = "managers_approval", nullable = false)
	private boolean managers_approval;
	
	 @Column(name = "number_of_closures")
	private Integer number_of_closures=0;
	
	@CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
	 
	@UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
	
    // Business method to close the JD
    public void closeJobDescription() {
        this.JD_status = true;
        this.updatedAt = LocalDateTime.now();
    }

    // Business method to increment registrations
    public void incrementRegistrations() {
        this.no_of_registrations = (this.no_of_registrations == null) ? 1 : this.no_of_registrations + 1;
    }
	
}
