package com.pentagon.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "student")
public class Student {
    @Id
    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;

    private String name;

    private String gender;

    private String email;

    @JsonIgnore
    private String password;

    private String mobile;
    
    private String profileUrl;
    
    private String profilePublicId;
    
    private String whatsappNo;
    
    private String dob;
    
    @Column(length = 1000)
    @Size(max = 1000, message = "Summary must be under 1000 characters")
    private String summary;
    
    private String address;
    
    private String state;
    
    private String city;
    
    private String experience;
    
    private String skills;
    
    private String tenthSchool;
    
    private Integer tenthPassOutYear;
    
    private Double tenthPercentage;
    
    private String twelveSchool;
    
    private Integer twelvePassOutYear;
    
    private Double twelvePercentage;
    
    private String gradSchool;
    
    private String gradCourse;
    
    private String gradBranch;
    
    private Double gradPercentage;
    
    private Double gradCgpa;
    
    private Integer gradPassOutYear;

    @Column(nullable = false)
    private String typeOfAdmission; // paid or CSR
    
    
    private String studyMode;
    
    @JsonIgnore
    private String passwordResetToken;
    
    @JsonIgnore
    private LocalDateTime passwordTokenExpiredAt;
    
    @Size(max = 50000)
    private String github;
    
    @Size(max = 50000)
    private String linkedin;
    
    @Size(max = 50000)
    private String resumeUrl;
    
    private String resumePublicId; 

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    public enum EnrollmentStatus {
        PENDING,
        ACTIVE,
        DISABLED,
        COMPLETED,
        DROPPED,
        PLACED
    }

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "batchId")
    private Batch batch;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "stackId")
    private Stack stack;
}