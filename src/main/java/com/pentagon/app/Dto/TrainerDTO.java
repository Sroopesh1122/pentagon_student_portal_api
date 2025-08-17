package com.pentagon.app.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.pentagon.app.entity.OrganizationBranch;
import com.pentagon.app.entity.Technology;

import lombok.Data;

@Data
public class TrainerDTO {

	private String trainerId;
    private String name;
    private String email;
    private String mobile;
//  private String trainerStack;
    private List<Technology> technologies;
    private String qualification;
    private Integer yearOfExperiences;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String programHeadId;
    private String gender;
    private String bio;
    private String profileImgUrl;
    private String profilePublicId;
    private LocalDate dob;
    private OrganizationBranch branch;
    
}
