package com.pentagon.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(length = 100, nullable = false)
    private String name;

    @Email(message = "Invalid email format")
    @Column(length = 150, nullable = false)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(length = 20, nullable = false)
    private String phoneNo;

    @Size(max = 10000)
    @Column(length = 10000)
    private String message;

    @Column(name = "is_closed", nullable = false)
    private boolean closed = false;

    @Column(length = 100)
    private String action;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
