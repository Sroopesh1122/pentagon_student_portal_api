package com.pentagon.app.response;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ProfileResponse {
	
	private String uniqueId;
	private String name;
	private String email;
	private String mobile;
	

}
