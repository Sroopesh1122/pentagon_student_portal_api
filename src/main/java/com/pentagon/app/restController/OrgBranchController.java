package com.pentagon.app.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.entity.OrganizationBranch;
import com.pentagon.app.exception.BranchException;
import com.pentagon.app.request.CreateNewOrgBranch;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.OrgBranchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/org-branch")
public class OrgBranchController
{
	
	@Autowired
	private OrgBranchService orgBranchService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/secure/create")
	public ResponseEntity<?> createNewOrgBranch(@Valid @RequestBody CreateNewOrgBranch request,BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
		{
			throw new BranchException("Invalid data Input", HttpStatus.BAD_REQUEST);
		}
		
		OrganizationBranch organizationBranch = new OrganizationBranch();
		organizationBranch.setBranchAddress(request.getBranchAddress());
		organizationBranch.setBranchName(request.getBranchName());
		organizationBranch.setHeadOffice(request.getIsHeadOffice());
		orgBranchService.create(organizationBranch);
		return ResponseEntity.ok(new ApiResponse<>("success","New Branch Created Successfully",null));
		
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/secure/{id}/block")
	public ResponseEntity<?> blockBranch(@PathVariable String id)
	{
		
		
		OrganizationBranch organizationBranch = orgBranchService.getById(id);
		
		if(organizationBranch ==null)
		{
			throw new BranchException("Branch Not Found", HttpStatus.BAD_REQUEST);
		}
		
		organizationBranch.setIsActive(false);
		orgBranchService.update(organizationBranch);
		return ResponseEntity.ok(new ApiResponse<>("success","Branch Blocked Successfully",null));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/secure/{id}/unblock")
	public ResponseEntity<?> unblockBranch(@PathVariable String id)
	{
		
		
		OrganizationBranch organizationBranch = orgBranchService.getById(id);
		
		if(organizationBranch ==null)
		{
			throw new BranchException("Branch Not Found", HttpStatus.BAD_REQUEST);
		}
		
		organizationBranch.setIsActive(true);
		orgBranchService.update(organizationBranch);
		return ResponseEntity.ok(new ApiResponse<>("success","Branch Un-Blocked Successfully",null));
		
	}
	
	
	
	@GetMapping("/public/")
	public ResponseEntity<?> getBranch(
			@RequestParam(required = false) Boolean active
			)
	{
		
		List<OrganizationBranch> branches = orgBranchService.getAllBranches(active);
		
		return ResponseEntity.ok(new ApiResponse<>("success","Branches Data",branches));
		
	}

}
 