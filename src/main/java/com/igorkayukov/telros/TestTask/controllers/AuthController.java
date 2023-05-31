package com.igorkayukov.telros.TestTask.controllers;

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

import com.igorkayukov.telros.TestTask.dto.Auth.Request.LoginRequest;
import com.igorkayukov.telros.TestTask.dto.Auth.Response.AuthResponse;
import com.igorkayukov.telros.TestTask.errors.ErrorMessage;
import com.igorkayukov.telros.TestTask.exceptions.NotValidAuthException;
import com.igorkayukov.telros.TestTask.security.JWTUtil;
import com.igorkayukov.telros.TestTask.services.AuthService;
import com.igorkayukov.telros.TestTask.validators.AuthRequestValidator;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final JWTUtil jwtUtil;
	private final AuthRequestValidator authDTOValidator;
	private final AuthService authServise;

	@Autowired
	public AuthController(JWTUtil jwtUtil, AuthRequestValidator authDTOValidator, AuthService authServise) {
		this.jwtUtil = jwtUtil;
		this.authDTOValidator = authDTOValidator;
		this.authServise = authServise;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> performLogin(@RequestBody @Valid LoginRequest loginRequest,
		BindingResult bindingResult, HttpServletResponse response) {

		authDTOValidator.validate(loginRequest, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new NotValidAuthException(ErrorMessage.getErrorMessage(bindingResult));
		}
		
		response.addCookie(authServise.getRefreshTokenCookieForLogin(loginRequest));

		return new ResponseEntity<>(authServise.getAuthResponseForLogin(loginRequest), HttpStatus.OK);
	}

	@PostMapping("/refresh_tokens")
	public ResponseEntity<AuthResponse> performLogin(
		@CookieValue(value = "refreshToken", required = false) String cookieValue, HttpServletResponse response) {
		
		if (cookieValue == null) {
			throw new NotValidAuthException("Refresh token cookie is not found. Perfom login!");
		}

		if (!jwtUtil.verifyToken(cookieValue)) {
			throw new NotValidAuthException("Refresh token cookie is not valid. Perfom login!");
		}
		
		response.addCookie(authServise.getRefreshTokenCookieForRefresh(cookieValue));

		return new ResponseEntity<>(authServise.getAuthResponseForRefresh(cookieValue), HttpStatus.OK);
	}
}