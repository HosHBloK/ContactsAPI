package com.igorkayukov.telros.TestTask.exceptions;

public class PersonNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4496379911420918428L;

	public PersonNotFoundException(String msg) {
		super(msg);
	}
}