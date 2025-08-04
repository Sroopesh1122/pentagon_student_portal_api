package com.pentagon.app.response;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class ExecutiveDetails 
{
    private Integer Id;
	private String executiveId;
	private String name;
	private String email;
	private String mobile;
    private boolean  active;
	private LocalDateTime createdAt;	
	private String managerId;
	private Map<String, Long> jdsCount;
	
	private String managerName;
	private String managerEmail;
	
	private String profileImgUrl;
}
