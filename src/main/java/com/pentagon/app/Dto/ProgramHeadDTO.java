package com.pentagon.app.Dto;

import java.time.LocalDateTime;
import java.util.List;

import com.pentagon.app.entity.Stack;
import com.pentagon.app.entity.Trainer;

import lombok.Data;


@Data
public class ProgramHeadDTO
{
	private String id;

	private String name;

	private String email;

	private String password;

	private List<Stack> stacks;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
	
	private Trainer trainer;
	
	private String profileImgUrl;
}
