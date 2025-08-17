package com.pentagon.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateEnquiryRequest 
{ 
 @NotBlank	
 private String name;
 
 @NotBlank
 private String phone;
 
 @NotBlank
 private String message;
 
 @NotBlank
 private String email;
}
