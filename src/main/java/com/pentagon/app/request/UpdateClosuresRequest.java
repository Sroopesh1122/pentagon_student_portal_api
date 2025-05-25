package com.pentagon.app.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateClosuresRequest {
	
    @NotBlank
    private String jobDescriptionId;
    
    @NotNull
    @Min(0)
    private Integer numberOfClosures;
}
