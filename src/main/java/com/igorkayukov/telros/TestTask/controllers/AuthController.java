package com.igorkayukov.telros.TestTask.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igorkayukov.telros.TestTask.dto.AuthDTO;
import com.igorkayukov.telros.TestTask.exceptions.NotValidAuthDTOException;
import com.igorkayukov.telros.TestTask.security.JWTUtil;
import com.igorkayukov.telros.TestTask.util.AuthResponse;
import com.igorkayukov.telros.TestTask.util.ErrorMessage;
import com.igorkayukov.telros.TestTask.util.ErrorResponse;
import com.igorkayukov.telros.TestTask.validators.AuthDTOValidator;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final JWTUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	private final AuthDTOValidator authDTOValidator;
	
	@Autowired
	public AuthController(JWTUtil jwtUtil, AuthenticationManager authenticationManager,
		AuthDTOValidator authDTOValidator) {
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
		this.authDTOValidator = authDTOValidator;
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> performLogin(@RequestBody @Valid AuthDTO authDTO,
		BindingResult bindingResult) {

		authDTOValidator.validate(authDTO, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new NotValidAuthDTOException(ErrorMessage.getErrorMessage(bindingResult));
		}

		UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(authDTO
			.getUsername(), authDTO.getPassword());

		authenticationManager.authenticate(authInputToken);

		String token = jwtUtil.generateToken(authDTO.getUsername());

		AuthResponse response = new AuthResponse(token);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@ExceptionHandler
	private ResponseEntity<ErrorResponse> handleLoginException(AuthenticationException ex) {

		ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	private ResponseEntity<ErrorResponse> handleCreateException(NotValidAuthDTOException ex) {

		ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}