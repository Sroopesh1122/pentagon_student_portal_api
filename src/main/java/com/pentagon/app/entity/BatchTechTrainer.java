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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch-id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "technology-id")
    private Technology technology;

    @ManyToOne
    @JoinColumn(name = "trainer-id")
    private Trainer trainer;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
}