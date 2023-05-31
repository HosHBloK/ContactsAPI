package com.hoshblok.ContactsAPI.services;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hoshblok.ContactsAPI.dto.auth.request.LoginRequest;
import com.hoshblok.ContactsAPI.dto.auth.response.AuthResponse;
import com.hoshblok.ContactsAPI.security.JWTUtil;

@Service
public class AuthServiseImpl implements AuthService {

	private final JWTUtil jwtUtil;
	private final AuthenticationManager authenticationManager;

	@Autowired
	public AuthServiseImpl(JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Cookie getRefreshTokenCookieForLogin(LoginRequest request) {

		return jwtUtil.generateRefreshTokenCookie(request.getUsername());
	}

	@Override
	public Cookie getRefreshTokenCookieForRefresh(String cookieWithToken) {

		String username = jwtUtil.retrieveClaim(jwtUtil.getDecodedToken(cookieWithToken)).get("username");

		return jwtUtil.generateRefreshTokenCookie(username);
	}

	@Override
	public AuthResponse getAuthResponseForLogin(LoginRequest request) {

		UsernamePasswordAuthenticationToken authRequestToken = new UsernamePasswordAuthenticationToken(request
			.getUsername(), request.getPassword());

		authenticationManager.authenticate(authRequestToken);

		return new AuthResponse(jwtUtil.generateAccessToken(request.getUsername()));
	}

	@Override
	public AuthResponse getAuthResponseForRefresh(String refreshToken) {

		DecodedJWT decodedToken = jwtUtil.getDecodedToken(refreshToken);

		String username = jwtUtil.retrieveClaim(decodedToken).get("username");

		return new AuthResponse(jwtUtil.generateAccessToken(username));
	}
}