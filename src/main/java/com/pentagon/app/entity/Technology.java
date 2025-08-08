package com.pentagon.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Technology {
    @Id
    private String techId;
   
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "technologies")
    private List<Stack> stacks;

    @ManyToMany(mappedBy = "technologies")
    @JsonIgnore
    private List<Trainer> trainers;
    
    private String logo;
    
}
