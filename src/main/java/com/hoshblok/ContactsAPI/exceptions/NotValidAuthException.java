package com.hoshblok.ContactsAPI.exceptions;

public class NotValidAuthException extends RuntimeException {

	private static final long serialVersionUID = 3213536618352849270L;

	public NotValidAuthException(String msg) {
		super(msg);
	}
}