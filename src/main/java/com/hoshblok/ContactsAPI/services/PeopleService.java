package com.hoshblok.ContactsAPI.services;

import java.util.List;

import com.hoshblok.ContactsAPI.dto.person.request.PersonContactInfoRequest;
import com.hoshblok.ContactsAPI.dto.person.request.PersonDetailedInfoRequest;
import com.hoshblok.ContactsAPI.dto.person.response.PersonContactInfoResponse;
import com.hoshblok.ContactsAPI.dto.person.response.PersonDetailedInfoResponse;
import com.hoshblok.ContactsAPI.dto.person.response.PersonResponse;
import com.hoshblok.ContactsAPI.models.Person;

public interface PeopleService {

	Person findOne(int id);

	PersonContactInfoResponse getPersonContactInfoResponse(int id);

	PersonDetailedInfoResponse getPersonDetailedInfoResponse(int id);

	PersonResponse getPersonResponse(int id);

	List<PersonResponse> getPersonResponseList();

	List<PersonContactInfoResponse> getPersonContactInfoResponseList();

	List<PersonDetailedInfoResponse> getPersonDetailedInfoResponseList();

	List<Person> findAll();

	byte[] getPhoto(int id);

	void addPersonPhoto(byte[] fileBytes, String fileName, int id);

	void save(PersonContactInfoRequest request);

	void save(PersonDetailedInfoRequest request);

	void updateContactInfo(int id, PersonContactInfoRequest request);

	void updateDetailedInfo(int id, PersonDetailedInfoRequest request);

	void delete(int id);

	void deletePhoto(int id);

	void deleteDetailedInfo(int id);
}