package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.OrganizationBranch;
import com.pentagon.app.repository.OrganizarionBranchRepository;
import com.pentagon.app.service.OrgBranchService;
import com.pentagon.app.utils.IdGeneration;

@Service
public class OrgBranchServiceImpl implements OrgBranchService {
	
	
	

	@Autowired
	private OrganizarionBranchRepository repository;
	
	@Autowired
	private IdGeneration idGeneration;
	
	@Override
	public OrganizationBranch create(OrganizationBranch organizationBranch) {
		
		organizationBranch.setId("BRANCH-"+idGeneration.generateRandomString());
		organizationBranch.setCreatedAt(LocalDateTime.now());
		return repository.save(organizationBranch);
	}

	@Override
	public OrganizationBranch update(OrganizationBranch organizationBranch) {
		// TODO Auto-generated method stub
		return repository.save(organizationBranch);
	}

	@Override
	public OrganizationBranch getById(String id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public void delete(String id) {
		repository.deleteById(id);

	}

	@Override
	public List<OrganizationBranch> getAllBranches(Boolean active) {
		
		return repository.getBranches(active);
	}

}
