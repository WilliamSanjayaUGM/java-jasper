package com.manulife.java_jasper.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.manulife.java_jasper.exception.UserNotFoundException;
import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.repository.UserRepository;
import com.manulife.java_jasper.service.UserService;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService{
	private static final Logger LOGGER=LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final BroadcasterServiceImpl broadcaster;
	
	public UserServiceImpl(UserRepository userRepository, BroadcasterServiceImpl broadcaster) {
		this.userRepository=userRepository;
		this.broadcaster=broadcaster;
	}
	
	@Override
	public User save(@Valid User user) {
		if (user.getId() != null) {
	        Optional<User> existing = userRepository.findById(user.getId());
	        if (existing.isPresent()) {
	            // Optionally copy only updatable fields
	            User old = existing.get();
	            old.setName(user.getName());
	            old.setEmail(user.getEmail());
	            old.setPhoneNo(user.getPhoneNo());
	            old.setAddress(user.getAddress());
	            old.setMale(user.isMale());
	            old.setDateOfBirth(user.getDateOfBirth());
	            user = old;
	        }
	    }
	    User saved = userRepository.save(user);
	    broadcaster.broadcast();
	    return saved;
	}
	
	@Override
	public long deleteById(long id) {
		userRepository.deleteById(id);
		broadcaster.broadcast();
		return id;
	}
	
	@Override
	public User getUserById(long id) {
		User user= userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
		return user;
	}
	
	@Override
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	@Override
	public Page<User> findPaginatedUser(String name, LocalDate dob,Pageable pageable){
		if ((name == null || name.isEmpty()) && dob == null) {
	        return userRepository.findAll(pageable);
	    }
		
		return userRepository.findByFilters(
	            name != null ? "%" + name.toLowerCase() + "%" : null,
	            dob != null ? Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null,
	            pageable
	    );
	}
	
	@Override
	public long count(String name, LocalDate dob) {
		if ((name == null || name.isEmpty()) && dob == null) {
	        return userRepository.count();
	    }
		
		return userRepository.countByFilters(
	            name != null ? "%" + name.toLowerCase() + "%" : null,
	            dob != null ? Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null
	    );
	}
}
