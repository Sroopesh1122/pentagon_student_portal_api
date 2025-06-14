package com.pentagon.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table
@Entity
@Data

public class Profile {
	  @Id
	private String empId;
	private String publicUrl;
	private String publiId;

}
