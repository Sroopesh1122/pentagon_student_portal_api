package com.pentagon.app.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;


public class CustomUserDetails implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Admin admin;
	private Manager manager;
	private Executive executive;
	private Trainer trainer;
	
	public CustomUserDetails(Admin admin) {
		this.admin = admin;
	}
	
	public CustomUserDetails(Manager manager) {
		this.manager = manager;
	}
	
	public CustomUserDetails(Executive executive) {
		this.executive = executive;
	}
	
	public CustomUserDetails(Trainer trainer) {
		this.trainer = trainer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (admin != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (manager != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));
        } else if (executive != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_EXECUTIVE"));
        }else if (trainer != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_TRAINER"));
        }
        return List.of(); // Empty list if none
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
	
    public String getEmail() {
        if (admin != null) return admin.getEmail();
        if (manager != null) return manager.getEmail();
        if (executive != null) return executive.getEmail();
        if (trainer != null) return trainer.getEmail();
        return null;
    }

    public String getRole() {
        if (admin != null) return "ADMIN";
        if (manager != null) return "MANAGER";
        if (executive != null) return "EXECUTIVE";
        if (trainer != null) return "TRAINER";
        return "UNKNOWN";
    }

    public String getId() {
        if (admin != null) return admin.getAdminId();
        if (manager != null) return manager.getManagerId();
        if (executive != null) return executive.getExecutiveId();
        if (trainer != null) return trainer.getTrainerId();
        return null;
    }
    
    public Object get() {
    	if (admin != null) return admin;
        if (manager != null) return manager;
        if (executive != null) return executive;
        if (trainer != null) return trainer;
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
	
	public Trainer getTrainer() {
		return trainer;
	}
}