package com.pentagon.app.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class MockTest {

    @Id
    private String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "technology_id")
    private Technology technology;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    private String mockName;

    @Size(max = 50000)
    private String topic;

    private LocalDate mockDate;
    
    @JsonIgnore
    @OneToMany(mappedBy = "mockTest")
    private List<MockRating> mockRatings;
}