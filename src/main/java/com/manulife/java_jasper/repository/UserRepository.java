package com.manulife.java_jasper.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.manulife.java_jasper.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	@Query("SELECT u FROM User u WHERE "
			+ "(:name IS NULL OR LOWER(u.name) LIKE LOWER(:name)) AND "
		    + "(:dob IS NULL OR u.dateOfBirth = :dob)")
	Page<User> findByFilters(@Param("name") String name, @Param("dob") LocalDate dob, Pageable pageable);
	
	@Query("SELECT COUNT(u) FROM User u WHERE "
			+ "(:name IS NULL OR LOWER(u.name) LIKE LOWER(:name)) AND "
		    + "(:dob IS NULL OR u.dateOfBirth = :dob)")
	long countByFilters(@Param("name") String name, @Param("dob") LocalDate dob);
}
