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

    private String stackId;
}
