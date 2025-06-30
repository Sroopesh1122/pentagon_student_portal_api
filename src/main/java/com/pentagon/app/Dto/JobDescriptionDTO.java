package com.pentagon.app.Dto;

import java.time.LocalDateTime;

import com.pentagon.app.entity.Executive;

import lombok.Data;

@Data
public class JobDescriptionDTO {
	private String jobDescriptionId;
	private String companyLogo;
	private String companyName;
	private String website;
	private String role;
	private String stack;
	private String qualification;
	private String stream;
	private Double percentage;
	private Integer minYearOfPassing;
	private Integer maxYearOfPassing;
	private String salaryPackage;
	private Integer numberOfRegistrations;
	private Integer currentRegistrations;
	private Double mockRating;
	private String jdStatus;
	private boolean managerApproval;
	private Integer numberOfClosures;
	private boolean isClosed;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String location;
	private Executive executive;
	private String postedBy;
	private String description;
	private String managerId;
	private String managerName;
	private String skills;
	private String jdActionReason;
	private String bondDetails;
	private String salaryDetails;
	private String backlogs;
	private String acardemicGap;
	private LocalDateTime approvedDate;
	private String currentRound;

}
