package com.pentagon.app.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExecutiveJDStatusDTO {

	private int totalJDsPosted;
    private int openings;
    private int closures;
}
