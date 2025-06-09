package com.pentagon.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
public class Batch {
    @Id
    private String batchId;
    
    private String batchName;

    @ManyToOne
    private Stack stack; // ✅ each Batch belongs to one Stack

    @OneToMany(mappedBy = "batch")
    private List<Student> students;

    @OneToMany(mappedBy = "batch")
    private List<BatchTechTrainer> techTrainerAssignments; // ✅ new
}
