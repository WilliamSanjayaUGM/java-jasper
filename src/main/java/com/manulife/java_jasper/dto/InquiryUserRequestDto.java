package com.manulife.java_jasper.dto;

import java.time.LocalDate;

import com.manulife.java_jasper.util.PagingRequest;

public class InquiryUserRequestDto extends PagingRequest{
	private String name;
	private LocalDate dateOfBirth;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
