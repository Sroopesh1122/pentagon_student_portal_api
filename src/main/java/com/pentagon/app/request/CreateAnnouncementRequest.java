package com.pentagon.app.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAnnouncementRequest {

	@NotBlank
	private String title;
	
	@NotBlank
	private String description;
	
	private String link;
	
	private List<String> batchId;
	
	private String expiryDate;

}
