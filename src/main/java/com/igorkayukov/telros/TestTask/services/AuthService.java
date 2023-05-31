package com.igorkayukov.telros.TestTask.services;

import javax.servlet.http.Cookie;

import com.igorkayukov.telros.TestTask.dto.Auth.Request.LoginRequest;
import com.igorkayukov.telros.TestTask.dto.Auth.Response.AuthResponse;

public interface AuthService {

	Cookie getRefreshTokenCookieForLogin(LoginRequest request);

	Cookie getRefreshTokenCookieForRefresh(String cookieWithToken);

	AuthResponse getAuthResponseForLogin(LoginRequest request);

	AuthResponse getAuthResponseForRefresh(String refreshToken);

}