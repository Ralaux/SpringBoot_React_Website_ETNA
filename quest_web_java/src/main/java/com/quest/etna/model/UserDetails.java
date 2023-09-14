package com.quest.etna.model;

import com.quest.etna.model.User.UserRole;


//@Entity
public class UserDetails {
	
	private String username;
	private String role = UserRole.ROLE_USER.toString();
	
	public UserDetails() {};
	
	public UserDetails(String username, String role) {
		this.username = username;
		if (role != null) {
			this.role = role;
		}
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return this.username;
	}
	
	public String getRole() {
		return this.role;
	}
}
