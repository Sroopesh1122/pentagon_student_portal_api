package com.pentagon.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class BatchTechTrainer {
    @Id
    private String id;

    @ManyToOne
    private Batch batch;

    @ManyToOne
    private Technology technology;

    @ManyToOne
    private Trainer trainer;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}