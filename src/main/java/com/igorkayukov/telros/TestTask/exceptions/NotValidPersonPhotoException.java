package com.igorkayukov.telros.TestTask.exceptions;

public class NotValidPersonPhotoException extends RuntimeException {

	private static final long serialVersionUID = 4743949173735066377L;

	public NotValidPersonPhotoException(String msg) {
		super(msg);
	}
}