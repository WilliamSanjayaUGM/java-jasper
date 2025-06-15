package com.manulife.java_jasper.exception;

public class UserNotFoundException extends RuntimeException{
	public UserNotFoundException(long id) {
		super("User with ID "+ id + " not exist.");
	}
	
	public UserNotFoundException(String name) {
		super("User with Name / email "+ name + " not exist.");
	}
}
