package com.pentagon.app.request;

import lombok.Data;

@Data
public class UpdateStudentRequest {

	private String name;

	private String gender;

	private String email;

	private String mobile;

	private String profileUrl;

	private String profilePublicId;

	private String whatsappNo;

	private String dob;

	private String summary;

	private String address;

	private String state;

	private String city;

	private String experience;

	private String skills;

	private String tenthSchool;

	private Integer tenthPassOutYear;

	private Double tenthPercentage;

	private String twelveSchool;

	private Integer twelvePassOutYear;

	private Integer twelvePercentage;

	private String gradSchool;

	private String gradCourse;

	private String gradBranch;

	private Double gradPercentage;

	private Double gradCgpa;

}
