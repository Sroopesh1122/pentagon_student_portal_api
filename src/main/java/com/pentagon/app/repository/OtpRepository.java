package com.pentagon.app.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pentagon.app.entity.Otp;

import jakarta.transaction.Transactional;

public interface OtpRepository extends JpaRepository<Otp, Integer> {

	// Optional<Otp> findValidOtpByEmail(String email);
	@Query("SELECT o FROM Otp o WHERE o.email = :email AND o.createdAt >= :cutoff")
	Optional<Otp> findValidOtpByEmail(@Param("email") String email, @Param("cutoff") LocalDateTime cutoff);
	
	Optional<Otp> findByEmail(String email);
	
	@Modifying
    @Transactional
	void deleteByEmail(String email);

}
