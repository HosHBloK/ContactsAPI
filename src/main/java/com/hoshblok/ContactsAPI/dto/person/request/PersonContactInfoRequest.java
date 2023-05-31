package com.hoshblok.ContactsAPI.dto.person.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PersonContactInfoRequest {

	@Size(min = 1, max = 30, message = "Name should be between 1 and 30 characters!")
	@NotNull(message = "Name should not be empty")
	private String name;

	@Email(message = "Wrong email format!")
	@Size(min = 1, max = 30, message = "Email should be between 1 and 30 characters!")
	@NotNull(message = "Email should not be empty")
	private String email;

	@Pattern(regexp = "^[0-9+-]+$", message = "Only numbers and '+' or '-' characters are allowed in phone number!")
	@Size(min = 1, max = 30, message = "PhoneNumber should be between 1 and 30 characters!")
	@NotNull(message = "PhoneNumber should not be empty")
	private String phoneNumber;

	public PersonContactInfoRequest() {
	}

	public PersonContactInfoRequest(String name, String email, String phoneNumber) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "PersonContactInfoResponse [name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber + "]";
	}
}