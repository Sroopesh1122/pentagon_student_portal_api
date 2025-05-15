package com.pentagon.requestDTO;

import java.util.List;

import lombok.Data;

@Data
public class AddJobDescriptionRequest {

	private String companyName;
	private String website;
	private String description;
	private Double percentage;
	private Integer minYearOfPassing;
	private Integer maxYearOfPassing;
	private List<String> qualification;
	private List<String> stream;
	private List<String> stack;
	private String salaryPackage;
	private Integer noOfRegistraions;
	private Double mockRating;
	
}
