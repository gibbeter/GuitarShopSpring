package com.example.demo.user;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Validated
public class UserRegDTO {
	
//	@NotNull
//	private Integer userId;

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

//	public Integer getUserId() {
//		return userId;
//	}
//
//	public void setUserId(Integer userId) {
//		this.userId = userId;
//	}
	
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
}
