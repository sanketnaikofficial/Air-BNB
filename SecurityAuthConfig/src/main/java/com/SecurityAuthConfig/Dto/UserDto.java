package com.SecurityAuthConfig.Dto;

import com.SecurityAuthConfig.Enums.UserRole;

public class UserDto {
	private Long id;
	private String name;
	private String username;
	private String email;
	private String mobile;
	private String password;
	private UserRole role;
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	//Getters and Setters For Each variable above 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;      	}

	public String getName() {
		return name;	  }

	public void setName(String name) {
		this.name = name;	  }

	public String getUsername() {
		return username;	}

	public void setUsername(String username) {
		this.username = username;	}

	public String getEmail() {
		return email;	}

	public void setEmail(String email) {
		this.email = email; 	}

	public String getPassword() {
		return password;	}

	public void setPassword(String password) {
		this.password = password;	}
	
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
}

