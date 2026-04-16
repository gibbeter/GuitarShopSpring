package com.example.demo.user;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Validated
public class UserDTO {
	
	@NotNull
	private Integer userId;

	@NotNull
	@NotEmpty
	private String userName;
	@NotNull
	@NotEmpty
	private String userPass;
	@NotNull
	private String type;
	@NotNull
	private String userMail;
	@NotNull
	private String name;
	@NotNull
	private String surname;
	@NotNull
	private Integer phoneNumber;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String password) {
		this.userPass = password;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public UserDTO(@NotNull Integer userId, @NotNull @NotEmpty String userName, @NotNull @NotEmpty String password,
			@NotNull String type, @NotNull String userMail) {
		this.userId = userId;
		this.userName = userName;
		this.userPass = password;
		this.type = type;
		this.userMail = userMail;
	}
	
	

	public UserDTO(@NotNull Integer userId, @NotNull @NotEmpty String userName, @NotNull @NotEmpty String userPass,
			@NotNull String type, @NotNull String userMail, @NotNull String name, @NotNull String surname,
			@NotNull Integer phoneNumber) {
		this.userId = userId;
		this.userName = userName;
		this.userPass = userPass;
		this.type = type;
		this.userMail = userMail;
		this.name = name;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
	}

	public UserDTO() {
		
	}
	
	
}
