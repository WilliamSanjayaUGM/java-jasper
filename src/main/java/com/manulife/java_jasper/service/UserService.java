package com.manulife.java_jasper.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.manulife.java_jasper.model.User;

import jakarta.validation.Valid;

public interface UserService {
	public User save(@Valid User user);
	
	public long deleteById(long id);
	
	public User getUserById(long id) ;
	
	public List<User> findAll();
	
	public Page<User> findPaginatedUser(String name, LocalDate dob,Pageable pageable);
	
	public long count(String name, LocalDate dob) ;
}
