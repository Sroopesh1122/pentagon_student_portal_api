package com.pentagon.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class BatchTechTrainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String batchId;
    
    private String techId;
    
    private String trainerId;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}