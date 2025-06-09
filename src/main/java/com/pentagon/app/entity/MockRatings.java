package com.pentagon.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class MockRatings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    private String studentId;
    
    private String trachId;
    
    private String trainerId;

    private Double rating;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}