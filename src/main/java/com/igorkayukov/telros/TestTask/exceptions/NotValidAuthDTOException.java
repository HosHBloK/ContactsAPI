package com.igorkayukov.telros.TestTask.exceptions;

public class NotValidAuthDTOException extends RuntimeException {

	private static final long serialVersionUID = 3213536618352849270L;

	public NotValidAuthDTOException(String msg) {
		super(msg);
	}
}