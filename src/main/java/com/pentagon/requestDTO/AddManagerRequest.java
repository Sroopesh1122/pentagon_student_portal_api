package com.pentagon.requestDTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AddManagerRequest {
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false, length = 10)
	private String mobile;
}
