package com.pentagon.requestDTO;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddJobDescriptionRequest {
	@Column(nullable = false)
	private String companyName;
	@Column(nullable = false)
	private String website;
	@Column(nullable = false)
	@Size(max = 50000)
	private String description;
	@Column(nullable = false)
	private Double percentage;
	@Column(nullable = false)
	private Integer minYearOfPassing;
	@Column(nullable = false)
	private Integer maxYearOfPassing;
	@Column(nullable = false)
	private String qualification;
	@Column(nullable = false)
	private String stream;
	@Column(nullable = false)
	private String stack;
	@Column(nullable = false)
	private String salaryPackage;
	@Column(nullable = false)
	private Integer noOfRegistraions;
	@Column(nullable = false)
	private Double mockRating;
	
}
