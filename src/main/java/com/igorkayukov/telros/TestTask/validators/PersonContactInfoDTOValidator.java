package com.igorkayukov.telros.TestTask.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.igorkayukov.telros.TestTask.dto.PersonContactInfoDTO;
import com.igorkayukov.telros.TestTask.repositories.PeopleRepository;

@Component
public class PersonContactInfoDTOValidator implements Validator{
	
	private final PeopleRepository peopleRepository;

	@Autowired
	public PersonContactInfoDTOValidator(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return PersonContactInfoDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		PersonContactInfoDTO personContactInfoDTO = (PersonContactInfoDTO) target;

		if (peopleRepository.findByEmail(personContactInfoDTO.getEmail()).isPresent()) {
			errors.rejectValue("email", "", "This email is already taken!");
		}
		
		if (peopleRepository.findByPhoneNumber(personContactInfoDTO.getPhoneNumber()).isPresent()) {
			errors.rejectValue("phoneNumber", "", "This phone number is already taken!");
		}
	}
}