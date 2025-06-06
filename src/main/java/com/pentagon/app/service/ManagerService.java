package com.pentagon.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.Dto.JdStatsDTO;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.request.ManagerLoginRequest;
import com.pentagon.app.response.ProfileResponse;

public interface ManagerService {
	
	
	public Long getTotalCount();

	public Manager addManager( Manager manager);
	
	public Manager updateManager(Manager manager);
	
	public void disableManagerByUniqueId(String managerId);
	
	public Manager getManagerByEmail(String email);

	String loginWithPassword(ManagerLoginRequest managerLoginRequest);

	public ProfileResponse getProfile(Manager manager);
	
	public Page<Manager> findAll(String q ,Pageable pageable);
	
	public Manager getManagerById(String managerId);
	
    public Object getManagersJdDetails(String managetId);
    
    public Long  getAllExecutivesCount(String managerId);
    
    public List<JdStatsDTO> getManagerJdStats(String managerId,String timeUnit ,Integer range);
    
    public Page<Executive> getAllExecutives(String managerId,String q , Pageable pageable);

}
