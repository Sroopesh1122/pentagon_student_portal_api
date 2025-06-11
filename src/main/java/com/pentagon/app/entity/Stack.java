package com.pentagon.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
public class Stack {
    @Id
    private String stackId;

    private String name;

    @ManyToMany
    private List<Technology> technologies;

    @ManyToMany
    private List<ProgramHead> programHeads;

    @OneToMany(mappedBy = "stack")
    private List<Batch> batches;

}