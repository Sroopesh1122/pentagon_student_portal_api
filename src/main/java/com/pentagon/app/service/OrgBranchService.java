package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.OrganizationBranch;

public interface OrgBranchService
{
  public OrganizationBranch create(OrganizationBranch organizationBranch);
  public OrganizationBranch update(OrganizationBranch organizationBranch);
  public OrganizationBranch getById(String id);
  public void delete(String id);
  public List<OrganizationBranch> getAllBranches(Boolean active);
}
