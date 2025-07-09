package com.pentagon.app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="job_description")
public class JobDescription {
	
    @Id
    @Column(name = "job_description_id", unique = true)
	private String jobDescriptionId;
    
    @Column( nullable = false )
    @Size(max = 50000)
   	private String companyLogo = "https://cdn-icons-png.flaticon.com/512/4537/4537992.png";
	
    @Column(name = "company_name", nullable = false)
    @Size(max = 50000)
	private String companyName;
	
    @Column(name = "website", nullable = false)
    @Size(max = 50000)
	private String website;
	
    @Column(name="description",nullable = false)
    @Size(max = 50000) 
    private String Description;
    
    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false)
    @Size(max = 50000)
	private String qualification;
	
    @Column(nullable = false)
    @Size(max = 50000)
	private String stream;
    
    @Column(nullable = false)
    @Size(max = 100000)
    private String skills;
    
    @Column(nullable = false)
	private Double percentage;
	
	@Column(name = "min_year_of_passing")
	private Integer minYearOfPassing;
	
	@Column(name = "max_year_of_passing")
	private Integer maxYearOfPassing;
	
	@Column(name="stack")
	@Size(max = 50000)
	private String stack;
	
	@Column(name="package",nullable = false)
	private String salaryPackage;
	
	@Column(name = "no_of_registrations")
	private Integer numberOfRegistrations=0;
	
	@Column(name = "current_registrations")
	private Integer currentRegistrations=0;
	
	@Column(name = "mock_rating")
	private Double mockRating;
	
	@Column(name = "JD_status", nullable = false)
	private String jdStatus;
	
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
	
	@JsonIgnore
	@ManyToOne
	private Executive executive;
	
	private String postedBy;
	
	private String location;
	
	@Size(max = 100000)
	private String jdActionReason;
	
	private String managerId;
	
	private LocalDateTime approvedDate;
	
	@Size(max = 100000)
	private String bondDetails;
	
	@Size(max = 100000)
	private String salaryDetails;
	
	private String backlogs;
	
	private String acardemicGap;
	
	private String currentRound = "Pending Scheduling";
	
	private String genderPreference;
	
	@Size(max = 50000)
	private String aboutCompany;
	
	private LocalDate interviewDate;
	
	@Size(max = 100000)
	private String rolesAndResponsibility;
	
	
	@OneToMany(mappedBy = "jobDescription")
	private List<JdRoundHistory> roundHistory;
	
	@OneToMany(mappedBy = "jobDescription")
	private List<JdStatusHistory> stautsHistory;
	
	//update current registration count
	public void updateCurrentRegistrations(int newCount) {
        if (newCount < 0) {
            throw new IllegalArgumentException("Registration count cannot be negative");
        }
        this.currentRegistrations = newCount;
        checkAndAutoClose();
    }
	
	private void checkAndAutoClose() {
        if (!isClosed && 
            numberOfRegistrations != null && 
            currentRegistrations != null &&
            currentRegistrations >= numberOfRegistrations) {
            closeJobDescription();
        }
    }

    public void closeJobDescription() {
        this.isClosed = true;
    } 
    
   
}