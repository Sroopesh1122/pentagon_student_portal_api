package com.pentagon.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "student")
public class Student {
    @Id
    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;

    @Column(nullable = false)
    private String name;

    private String gender;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String mobile;

    @Column(nullable = false)
    private String stack;

    @Column(nullable = false)
    private String typeOfAdmission; // paid or CSR

    private String mode; // offline, online
    private String address;
    private String college;
    private String qualification;
    private Integer yearOfPassout;
    private Double perecentage;
    private String stream;

    @Size(max = 50000)
    private String objective;

    @Size(max = 50000)
    private String skills;

    @Size(max = 50000)
    private String internship;

    @Size(max = 50000)
    private String projects;

    private Double mockRating;

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
    private Batch batch;
}