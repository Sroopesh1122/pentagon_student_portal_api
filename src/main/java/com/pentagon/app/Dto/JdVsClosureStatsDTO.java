package com.pentagon.app.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JdVsClosureStatsDTO
{
	private String label;
	private Long jdCount;
	private Long closureCount;
}
