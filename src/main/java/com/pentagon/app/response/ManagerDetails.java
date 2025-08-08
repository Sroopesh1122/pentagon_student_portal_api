package com.pentagon.app.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.pentagon.app.Dto.JdStatsDTO;

import lombok.Data;

@Data
public class ManagerDetails {

	private Integer Id;
	private String managerId;
	private String name;
	private String email;
	private String mobile;
	private String password;
	private boolean active;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Map<String, Long> jdsCount;
	private Long totalExecutives;
	private List<JdStatsDTO> lastWeekJdCount;
	private String profileImgUrl;
	
}
