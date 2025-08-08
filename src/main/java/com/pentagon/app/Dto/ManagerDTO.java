package com.pentagon.app.Dto;

import java.time.LocalDateTime;
import lombok.Data;
@Data
public class ManagerDTO {

    private Integer Id;
	private String managerId;
	private String name;
	private String email;
	private String mobile;
	private String password;
	private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String profileImgUrl;
}
