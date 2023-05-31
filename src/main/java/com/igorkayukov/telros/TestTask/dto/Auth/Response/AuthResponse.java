package com.igorkayukov.telros.TestTask.dto.Auth.Response;

public class AuthResponse {
	
	private String jwt_access_token;
	
	public AuthResponse(String jwt_access_token) {
		this.jwt_access_token = jwt_access_token;
	}

	public String getJwt_access_token() {
		return jwt_access_token;
	}

	public void setJwt_access_token(String jwt_access_token) {
		this.jwt_access_token = jwt_access_token;
	}
}