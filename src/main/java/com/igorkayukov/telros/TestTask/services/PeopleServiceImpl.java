package com.igorkayukov.telros.TestTask.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorkayukov.telros.TestTask.Mappings.PersonMapping;
import com.igorkayukov.telros.TestTask.dto.Person.Request.PersonContactInfoRequest;
import com.igorkayukov.telros.TestTask.dto.Person.Request.PersonDetailedInfoRequest;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonContactInfoResponse;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonDetailedInfoResponse;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonResponse;
import com.igorkayukov.telros.TestTask.models.Person;
import com.igorkayukov.telros.TestTask.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class PeopleServiceImpl implements PeopleService {

	private final PeopleRepository peopleRepository;
	private final PersonMapping personMapping;

	public PeopleServiceImpl(PeopleRepository peopleRepository, PersonMapping personMapping) {
		this.peopleRepository = peopleRepository;
		this.personMapping = personMapping;
	}

	@Override
	public Person findOne(int id) {
		Optional<Person> person = peopleRepository.findById(id);
		return person.orElse(null);
	}

	@Override
	public PersonContactInfoResponse getPersonContactInfoResponse(int id) {

		return personMapping.convertToPersonContactInfoResponse(findOne(id));
	}

	@Override
	public PersonDetailedInfoResponse getPersonDetailedInfoResponse(int id) {

		return personMapping.convertToPersonDetailedInfoResponse(findOne(id));
	}

	@Override
	public PersonResponse getPersonResponse(int id) {

		return personMapping.convertToPersonResponse(findOne(id));
	}

	@Override
	public List<PersonResponse> getPersonResponseList() {

		return personMapping.convertToPersonResponseList(findAll());
	}

	@Override
	public List<PersonContactInfoResponse> getPersonContactInfoResponseList() {

		return personMapping.convertToPersonContactInfoResponseList(findAll());
	}

	@Override
	public List<PersonDetailedInfoResponse> getPersonDetailedInfoResponseList() {

		return personMapping.convertToPersonDetailedInfoResponseList(findAll());
	}

	@Override
	public List<Person> findAll() {
		return peopleRepository.findAll();
	}

	@Override
	public byte[] getPhoto(int id) {
		return findOne(id).getPhoto();
	}

	@Override
	@Transactional
	public void addPersonPhoto(byte[] fileBytes, String fileName, int id) {

		Person person = findOne(id);
		person.setPhoto(fileBytes);
		person.setPhotoName(fileName);

		peopleRepository.save(person);
	}

	@Override
	@Transactional
	public void save(PersonContactInfoRequest request) {

		peopleRepository.save(personMapping.convertToPerson(request));
	}

	@Override
	@Transactional
	public void save(PersonDetailedInfoRequest request) {

		peopleRepository.save(personMapping.convertToPerson(request));
	}

	@Override
	@Transactional
	public void updateContactInfo(int id, PersonContactInfoRequest request) {

		Person person = findOne(id);
		person.setName(request.getName());
		person.setEmail(request.getEmail());
		person.setPhoneNumber(request.getPhoneNumber());
		peopleRepository.save(person);
	}

	@Override
	@Transactional
	public void updateDetailedInfo(int id, PersonDetailedInfoRequest request) {

		Person updatedPerson = personMapping.convertToPerson(request);

		Person person = findOne(id);
		person.setSurname(updatedPerson.getSurname());
		person.setPatronymic(updatedPerson.getPatronymic());
		person.setDateOfBirth(updatedPerson.getDateOfBirth());

		peopleRepository.save(person);
	}

	@Override
	@Transactional
	public void delete(int id) {

		peopleRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void deletePhoto(int id) {

		Person person = findOne(id);
		person.setPhoto(null);
		person.setPhotoName(null);

		peopleRepository.save(person);
	}

	@Override
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
}