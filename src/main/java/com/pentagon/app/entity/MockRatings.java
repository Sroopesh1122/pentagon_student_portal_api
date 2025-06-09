package com.pentagon.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class MockRatings {
    @Id
    private String id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Trainer trainer;

    @ManyToOne
    private Technology technology;

    private Double rating;
    
    private String feedback;

    @CreationTimestamp
    private LocalDateTime createdAt;
}