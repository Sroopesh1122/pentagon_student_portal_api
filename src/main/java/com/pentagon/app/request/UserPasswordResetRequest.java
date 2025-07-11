package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPasswordResetRequest
{

	@NotBlank
	  private String resetToken;
	  
	  @NotBlank
	  private String newPassword;
	  
	  @NotBlank
	  private String role;
}
