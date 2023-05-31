package com.hoshblok.ContactsAPI.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hoshblok.ContactsAPI.dto.person.request.PersonContactInfoRequest;
import com.hoshblok.ContactsAPI.repositories.PeopleRepository;

@Component
public class PersonContactInfoRequestValidator implements Validator{
	
	private final PeopleRepository peopleRepository;

	@Autowired
	public PersonContactInfoRequestValidator(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return PersonContactInfoRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		PersonContactInfoRequest personContactInfoRequest = (PersonContactInfoRequest) target;

		if (peopleRepository.findByEmail(personContactInfoRequest.getEmail()).isPresent()) {
			errors.rejectValue("email", "", "This email is already taken!");
		}
		
		if (peopleRepository.findByPhoneNumber(personContactInfoRequest.getPhoneNumber()).isPresent()) {
			errors.rejectValue("phoneNumber", "", "This phone number is already taken!");
		}
	}
}