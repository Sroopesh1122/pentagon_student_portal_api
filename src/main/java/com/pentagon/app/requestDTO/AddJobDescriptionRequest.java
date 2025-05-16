package com.pentagon.app.requestDTO;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddJobDescriptionRequest {
	@NotBlank
	private String companyName;
	@NotBlank
	private String website;
	@Size(max = 50000)
	@NotBlank
	private String description;
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
	@NotBlank
	private String stack;
	@NotBlank
	private String salaryPackage;
	@NotNull
	@Min(value=1)
	private Integer noOfRegistraions;
	@NotNull
	private Double mockRating;
	
}
