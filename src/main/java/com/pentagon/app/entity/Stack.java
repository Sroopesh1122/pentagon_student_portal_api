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
    @JoinTable(
        name = "stack_technology",
        joinColumns = @JoinColumn(name = "stack_id"),
        inverseJoinColumns = @JoinColumn(name = "tech_id")
    )
    private List<Technology> technologies;
}