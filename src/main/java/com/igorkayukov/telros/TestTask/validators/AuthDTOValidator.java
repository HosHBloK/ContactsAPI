package com.igorkayukov.telros.TestTask.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.igorkayukov.telros.TestTask.dto.AuthDTO;
import com.igorkayukov.telros.TestTask.repositories.UserRepository;

@Component
public class AuthDTOValidator implements Validator {

	private final UserRepository userRepository;

	public AuthDTOValidator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return AuthDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		AuthDTO authDTO = (AuthDTO) target;

		if (userRepository.findByUsername(authDTO.getUsername()).isEmpty()) {
			errors.rejectValue("username", "", "User not found!");
		}
	}
}