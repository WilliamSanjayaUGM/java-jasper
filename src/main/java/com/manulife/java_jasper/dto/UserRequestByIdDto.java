package com.manulife.java_jasper.dto;

public class UserRequestByIdDto {
	private long id;
	
	public UserRequestByIdDto(long id) {
		this.id=id;
	}
	
	public UserRequestByIdDto() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
