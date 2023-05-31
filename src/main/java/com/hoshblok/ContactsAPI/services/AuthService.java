package com.hoshblok.ContactsAPI.services;

import javax.servlet.http.Cookie;

import com.hoshblok.ContactsAPI.dto.auth.request.LoginRequest;
import com.hoshblok.ContactsAPI.dto.auth.response.AuthResponse;

public interface AuthService {

	Cookie getRefreshTokenCookieForLogin(LoginRequest request);

	Cookie getRefreshTokenCookieForRefresh(String cookieWithToken);

	AuthResponse getAuthResponseForLogin(LoginRequest request);

	AuthResponse getAuthResponseForRefresh(String refreshToken);

}