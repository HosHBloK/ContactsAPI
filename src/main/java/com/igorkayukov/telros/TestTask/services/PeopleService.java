package com.igorkayukov.telros.TestTask.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorkayukov.telros.TestTask.dto.PersonContactInfoDTO;
import com.igorkayukov.telros.TestTask.dto.PersonDTO;
import com.igorkayukov.telros.TestTask.dto.PersonDetailedInfoDTO;
import com.igorkayukov.telros.TestTask.models.Person;
import com.igorkayukov.telros.TestTask.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class PeopleService {

	private final PeopleRepository peopleRepository;
	private final ModelMapper modelMapper;

	public PeopleService(PeopleRepository peopleRepository, ModelMapper modelMapper) {
		this.peopleRepository = peopleRepository;
		this.modelMapper = modelMapper;
	}

	public Person findOne(int id) {
		Optional<Person> person = peopleRepository.findById(id);
		return person.orElse(null);
	}

	public List<Person> findAll() {
		return peopleRepository.findAll();
	}

	public byte[] getPhoto(int id) {
		return findOne(id).getPhoto();
	}
	
	@Transactional
	public void addPersonPhoto(byte[] fileBytes, String fileName, int id) {
		
		Person person = findOne(id);
		person.setPhoto(fileBytes);
		person.setPhotoName(fileName);
		
		peopleRepository.save(person);
	}

	@Transactional
	public void save(Person person) {

		peopleRepository.save(person);
	}

	@Transactional
	public void updateContactInfo(int id, Person updatedPerson) {

		Person person = findOne(id);
		person.setName(updatedPerson.getName());
		person.setEmail(updatedPerson.getEmail());
		person.setPhoneNumber(updatedPerson.getPhoneNumber());
		peopleRepository.save(person);
	}
	
	@Transactional
	public void updateDetailedInfo(int id, Person updatedPerson) {
		
		Person person = findOne(id);
		person.setSurname(updatedPerson.getSurname());
		person.setPatronymic(updatedPerson.getPatronymic());
		person.setDateOfBirth(updatedPerson.getDateOfBirth());
		
		peopleRepository.save(person);
	}

	@Transactional
	public void delete(int id) {

		peopleRepository.deleteById(id);
	}

	@Transactional
	public void deletePhoto(int id) {

		Person person = findOne(id);
		person.setPhoto(null);
		person.setPhotoName(null);
		
		peopleRepository.save(person);
	}

	@Transactional
	public void deleteDetailedInfo(int id) {

		Person person = findOne(id);
		clearDetailedInfo(person);
		peopleRepository.save(person);
	}

	private void clearDetailedInfo(Person person) {

		person.setSurname(null);
		person.setPatronymic(null);
		person.setDateOfBirth(null);
	}

	public PersonDTO convertToPersonDTO(Person person) {

		return modelMapper.map(person, PersonDTO.class);
	}

	public PersonContactInfoDTO convertToPersonContactInfoDTO(Person person) {

		return modelMapper.map(person, PersonContactInfoDTO.class);
	}

	public PersonDetailedInfoDTO convertToPersonDetailedInfoDTO(Person person) {

		return modelMapper.map(person, PersonDetailedInfoDTO.class);
	}

	public Person convertToPerson(PersonContactInfoDTO personContactInfoDTO) {

		return modelMapper.map(personContactInfoDTO, Person.class);
	}

	public Person convertToPerson(PersonDetailedInfoDTO personDetailedInfoDTO) {

		return modelMapper.map(personDetailedInfoDTO, Person.class);
	}
}