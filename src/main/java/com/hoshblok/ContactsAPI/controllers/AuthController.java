package com.hoshblok.ContactsAPI.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoshblok.ContactsAPI.dto.auth.request.LoginRequest;
import com.hoshblok.ContactsAPI.dto.auth.response.AuthResponse;
import com.hoshblok.ContactsAPI.errors.ErrorMessage;
import com.hoshblok.ContactsAPI.exceptions.NotValidAuthException;
import com.hoshblok.ContactsAPI.security.JWTUtil;
import com.hoshblok.ContactsAPI.services.AuthService;
import com.hoshblok.ContactsAPI.validators.AuthRequestValidator;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final JWTUtil jwtUtil;
	private final AuthRequestValidator authDTOValidator;
	private final AuthService authService;

	@Autowired
	public AuthController(JWTUtil jwtUtil, AuthRequestValidator authDTOValidator, AuthService authServise) {
		this.jwtUtil = jwtUtil;
		this.authDTOValidator = authDTOValidator;
		this.authService = authServise;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> performLogin(@RequestBody @Valid LoginRequest loginRequest,
		BindingResult bindingResult, HttpServletResponse response) {

		authDTOValidator.validate(loginRequest, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new NotValidAuthException(ErrorMessage.getErrorMessage(bindingResult));
		}

		response.addCookie(authService.getRefreshTokenCookieForLogin(loginRequest));

		return new ResponseEntity<>(authService.getAuthResponseForLogin(loginRequest), HttpStatus.OK);
	}

	@PostMapping("/refresh_tokens")
	public ResponseEntity<AuthResponse> refreshTokens(
		@CookieValue(value = "refreshToken", required = false) String cookieValue, HttpServletResponse response) {

		if (cookieValue == null) {
			throw new NotValidAuthException("Refresh token cookie is not found. Perfom login!");
		}

		if (!jwtUtil.verifyToken(cookieValue)) {
			throw new NotValidAuthException("Refresh token cookie is not valid. Perfom login!");
		}

		response.addCookie(authService.getRefreshTokenCookieForRefresh(cookieValue));

		return new ResponseEntity<>(authService.getAuthResponseForRefresh(cookieValue), HttpStatus.OK);
	}
}