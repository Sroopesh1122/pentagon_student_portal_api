package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	Optional<Manager> findByEmail(String email);
	
	public Optional<Manager> findByManagerId(String managerId);

	@Query("SELECT COUNT(M) FROM Manager M")
	int getManagerCount();
	
	@Query("SELECT m FROM Manager m WHERE " + "(:email IS NULL OR LOWER(m.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND "
			+ "(:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
			+ "(:managerId IS NULL OR m.managerId = :managerId) AND "
			+ "(:number IS NULL OR m.mobile=:mobile)")
	Page<Manager> findByFilters(@Param("name") String name, @Param("mobile") String mobile,
			@Param("email") String email,@Param("managerId") String managerId, Pageable pageable);

}
