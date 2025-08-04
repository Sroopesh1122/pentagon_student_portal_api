package com.pentagon.app.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddJobDescriptionRequest {

	@NotBlank
	private String companyLogoUrl;
	@NotBlank
	private String companyName;
	@NotBlank
	private String website;
	@Size(max = 50000)
	@NotBlank
	private String description;
	@NotBlank
	private String role;
	@NotNull
	private Double percentage;
	@NotNull
	private Integer minYearOfPassing;
	@NotNull
	private Integer maxYearOfPassing;
	@NotBlank
	private String qualification;
	@NotBlank
	private String stream;
	
	private String stack;
	
	@NotBlank
	private String salaryPackage;
	
	@NotNull
	@Min(value = 1)
	private Integer noOfRegistrations;
	@NotNull
	private Double mockRating;
	@NotNull
	private String location;

	private String skills;

	private String bondDetails;

	private String salaryDetails;

	@NotNull
	private String backlogs;

	@NotNull
	private String acardemicGap;
	
	@NotNull
    private String genderPreference;
	
	@NotNull
	private String aboutCompany;
	
	@NotNull
	private LocalDate interviewDate;
	
	private String rolesAndResponsibility;
	
	
	private List<String> technologies;
	
	
	

}
