package com.igorkayukov.telros.TestTask.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class PersonDetailedInfoDTO {

	@Size(min = 1, max = 30, message = "Surname should be between 1 and 30 characters!")
	@NotNull(message = "Surname should not be empty")
	private String surname;

	@Size(min = 1, max = 30, message = "Patronymic should be between 1 and 30 characters!")
	@NotNull(message = "Patronymic should not be empty")
	private String patronymic;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "DateOfBirth should not be empty")
	private Date dateOfBirth;

	public PersonDetailedInfoDTO() {
	}

	public PersonDetailedInfoDTO(String surname, String patronymic, Date dateOfBirth) {
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return "PersonDetailedInfoDTO [surname=" + surname + ", patronymic=" + patronymic + ", dateOfBirth="
			+ dateOfBirth + "]";
	}
}