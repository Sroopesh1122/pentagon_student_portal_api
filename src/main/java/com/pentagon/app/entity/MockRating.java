package com.pentagon.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Entity
@Data
public class MockRating {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Double rating =0.0;

    private String remark;

    private boolean attendance = false;
    
    @JsonIgnore
    @ManyToOne
    private MockTest mockTest;
    
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}