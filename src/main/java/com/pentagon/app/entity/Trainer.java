package com.pentagon.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainers")
public class Trainer {
    @Id
    @Column(name = "trainer_id", nullable = false, unique = true)
    private String trainerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    private String mobile;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    private String qualification;
    
    private Integer yearOfExperiences;
    
    private String gender;
    
    @Size(max = 5000)
    private String bio;
    
    private String profileImgUrl = "https://img.freepik.com/premium-vector/vector-flat-illustration-grayscale-avatar-user-profile-person-icon-gender-neutral-silhouette-profile-picture-suitable-social-media-profiles-icons-screensavers-as-templatex9xa_719432-2210.jpg?semt=ais_hybrid&w=740";
    
    private String profilePublicId;
    
    private LocalDate dob;
    
    
    @JsonIgnore
    private String passwordResetToken;
    
    @JsonIgnore
    private LocalDateTime passwordTokenExpiredAt;
    

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    
    private String programHeadId;  // added by
    
    private boolean isProgramHead = false;
    
    @ManyToMany
    @JsonIgnore
    private List<Technology> technologies;
    
    
    @JoinColumn(name = "branch_id")
    @ManyToOne
    private OrganizationBranch branch;
    
    
    @JsonIgnore
    @OneToMany(mappedBy = "trainer")
    private List<BatchTechTrainer> batchTechTrainer;
    
    
}