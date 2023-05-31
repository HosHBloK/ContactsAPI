
package com.igorkayukov.telros.TestTask.Mappings;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.igorkayukov.telros.TestTask.dto.Person.Request.PersonContactInfoRequest;
import com.igorkayukov.telros.TestTask.dto.Person.Request.PersonDetailedInfoRequest;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonContactInfoResponse;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonDetailedInfoResponse;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonResponse;
import com.igorkayukov.telros.TestTask.models.Person;

@Component
public class PersonMapping {

	private final ModelMapper modelMapper;

	@Autowired
	public PersonMapping(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public List<PersonContactInfoResponse> convertToPersonContactInfoResponseList(List<Person> peopleList) {
		return peopleList.stream().map(this::convertToPersonContactInfoResponse).collect(Collectors.toList());
	}

	public List<PersonDetailedInfoResponse> convertToPersonDetailedInfoResponseList(List<Person> peopleList) {
		return peopleList.stream().map(this::convertToPersonDetailedInfoResponse).collect(Collectors.toList());
	}

	public List<PersonResponse> convertToPersonResponseList(List<Person> peopleList) {
		return peopleList.stream().map(this::convertToPersonResponse).collect(Collectors.toList());
	}

	public PersonResponse convertToPersonResponse(Person person) {

		return modelMapper.map(person, PersonResponse.class);
	}

	public PersonContactInfoResponse convertToPersonContactInfoResponse(Person person) {

		return modelMapper.map(person, PersonContactInfoResponse.class);
	}

	public PersonDetailedInfoResponse convertToPersonDetailedInfoResponse(Person person) {

		return modelMapper.map(person, PersonDetailedInfoResponse.class);
	}

	public Person convertToPerson(PersonContactInfoRequest personContactInfoDTO) {

		return modelMapper.map(personContactInfoDTO, Person.class);
	}

	public Person convertToPerson(PersonDetailedInfoRequest personDetailedInfoDTO) {

		return modelMapper.map(personDetailedInfoDTO, Person.class);
	}
}