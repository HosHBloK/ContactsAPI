package com.hoshblok.ContactsAPI.dto.person.response;

public class PersonContactInfoResponse {

	private String name;

	private String email;

	private String phoneNumber;

	public PersonContactInfoResponse() {
	}

	public PersonContactInfoResponse(String name, String email, String phoneNumber) {
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