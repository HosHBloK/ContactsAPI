package com.igorkayukov.telros.TestTask.services;

import java.util.List;

import com.igorkayukov.telros.TestTask.dto.Person.Request.PersonContactInfoRequest;
import com.igorkayukov.telros.TestTask.dto.Person.Request.PersonDetailedInfoRequest;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonContactInfoResponse;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonDetailedInfoResponse;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonResponse;
import com.igorkayukov.telros.TestTask.models.Person;

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