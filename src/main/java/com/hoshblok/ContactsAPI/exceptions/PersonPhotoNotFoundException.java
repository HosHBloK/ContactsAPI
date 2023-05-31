package com.hoshblok.ContactsAPI.exceptions;

public class PersonPhotoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3838695844469835176L;

	public PersonPhotoNotFoundException(String msg) {
		super(msg);
	}
}