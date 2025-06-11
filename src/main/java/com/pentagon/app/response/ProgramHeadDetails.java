package com.pentagon.app.response;

import java.time.LocalDateTime;
import java.util.List;

import com.pentagon.app.entity.ProgramHeadStack;

import lombok.Data;

@Data
public class ProgramHeadDetails {
    private String id;
    private String name;
    private String email;
    private String mobile;
    private String qualification;
    private Integer yearOfExperiences;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProgramHeadStack> programHeadStack;
}