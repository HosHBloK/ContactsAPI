package com.igorkayukov.telros.TestTask.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthDTO {

	@Size(min = 3, max = 15, message = "Username should be between 3 and 15 characters!")
	@NotNull(message = "Username should not be empty")
	private String username;

	@NotNull(message = "Password should not be empty!")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}