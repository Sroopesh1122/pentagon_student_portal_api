package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pentagon.app.entity.OrganizationBranch;

public interface OrganizarionBranchRepository extends JpaRepository<OrganizationBranch,String> {

	@Query("SELECT b FROM OrganizationBranch b WHERE :active IS NULL OR b.isActive= :active ORDER BY createdAt DESC")
	public List<OrganizationBranch> getBranches(Boolean active);
}
