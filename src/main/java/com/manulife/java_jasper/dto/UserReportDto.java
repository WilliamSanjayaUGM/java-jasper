package com.manulife.java_jasper.dto;

import java.time.ZoneId;
import java.util.Date;

import com.manulife.java_jasper.model.User;

public class UserReportDto {
	private String name;
    private String email;
    private String phoneNo;
    private String address;
    private Boolean male;
    private Date dateOfBirth;

    public UserReportDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNo = user.getPhoneNo();
        this.address = user.getAddress();
        this.male = user.isMale();
        this.dateOfBirth = user.getDateOfBirth() == null ? null :
            Date.from(user.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getMale() {
		return male;
	}

	public void setMale(Boolean male) {
		this.male = male;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
    
}
