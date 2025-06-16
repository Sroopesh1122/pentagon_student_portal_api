package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.app.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {

}
