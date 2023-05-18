package com.igorkayukov.telros.TestTask.util;

public class AuthResponse {
	
	private String jwt_token;
	
	public AuthResponse(String jwt_token) {
		this.jwt_token = jwt_token;
	}

	public String getJwt_token() {
		return jwt_token;
	}

	public void setJwt_token(String jwt_token) {
		this.jwt_token = jwt_token;
	}
}