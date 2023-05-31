package com.hoshblok.ContactsAPI.dto.person.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PersonDetailedInfoRequest {

	@Size(min = 1, max = 30, message = "Surname should be between 1 and 30 characters!")
	@NotNull(message = "Surname should not be empty")
	private String surname;

	@Size(min = 1, max = 30, message = "Patronymic should be between 1 and 30 characters!")
	@NotNull(message = "Patronymic should not be empty")
	private String patronymic;

	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Wrong date format, use 2000-12-31")
	@NotNull(message = "DateOfBirth should not be empty")
	private String dateOfBirth;

	public PersonDetailedInfoRequest() {
	}

	public PersonDetailedInfoRequest(String surname, String patronymic, String dateOfBirth) {
		this.surname = surname;
		this.patronymic = patronymic;
		this.dateOfBirth = dateOfBirth;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return "PersonDetailedInfoResponse [surname=" + surname + ", patronymic=" + patronymic + ", dateOfBirth="
			+ dateOfBirth + "]";
	}
}