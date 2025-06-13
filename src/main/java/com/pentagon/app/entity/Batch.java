package com.pentagon.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
public class Batch {
    @Id
    private String batchId;
    
    private String name;

    @ManyToOne
    private Stack stack;

    @JsonIgnore
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<BatchTechTrainer> batchTechTrainers;

    @OneToMany(mappedBy = "batch")
    @JsonIgnore
    private List<Student> students;
}
