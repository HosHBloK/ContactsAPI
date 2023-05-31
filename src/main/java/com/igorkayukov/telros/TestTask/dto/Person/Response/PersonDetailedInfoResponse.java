package com.igorkayukov.telros.TestTask.dto.Person.Response;

public class PersonDetailedInfoResponse {

	private String surname;

	private String patronymic;

	private String dateOfBirth;

	public PersonDetailedInfoResponse() {
	}

	public PersonDetailedInfoResponse(String surname, String patronymic, String dateOfBirth) {
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