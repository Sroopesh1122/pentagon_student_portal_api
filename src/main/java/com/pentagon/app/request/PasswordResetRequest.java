package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequest 
{
  @NotBlank
  private String resetToken;
  
  @NotBlank
  private String newPassword;
  
}
