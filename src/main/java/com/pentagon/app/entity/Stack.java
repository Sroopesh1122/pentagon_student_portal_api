package com.pentagon.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
public class Stack {
	@Id
	private String stackId;

	private String name;

	@ManyToMany
	@JsonIgnore
	private List<Technology> technologies;

	@ManyToMany(mappedBy = "stacks")
	@JsonIgnore
	private List<ProgramHead> programHeads;

	@OneToMany(mappedBy = "stack")
	@JsonIgnore
	private List<Batch> batches;
	
	
	@OneToMany(mappedBy = "stack")
	@JsonIgnore
	private List<Student> students;
	

}