package com.hoshblok.ContactsAPI.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hoshblok.ContactsAPI.dto.auth.request.LoginRequest;
import com.hoshblok.ContactsAPI.repositories.UserRepository;

@Component
public class AuthRequestValidator implements Validator {

	private final UserRepository userRepository;

	public AuthRequestValidator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		LoginRequest authRequest = (LoginRequest) target;

		if (userRepository.findByUsername(authRequest.getUsername()).isEmpty()) {
			errors.rejectValue("username", "", "User not found!");
		}
	}
}