package com.pentagon.Service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pentagon.Entity.Admin;
import com.pentagon.Entity.Executive;
import com.pentagon.Entity.Manager;


public class CustomUserDetails implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Admin admin;
	private Manager manager;
	private Executive executive;
	
	public CustomUserDetails(Admin admin) {
		this.admin = admin;
	}
	
	public CustomUserDetails(Manager manager) {
		this.manager = manager;
	}
	
	public CustomUserDetails(Executive executive) {
		this.executive = executive;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Admin getAdmin() {
	    return admin;
	}

	public Manager getManager() {
	    return manager;
	}

	public Executive getExecutive() {
	    return executive;
	}
}
