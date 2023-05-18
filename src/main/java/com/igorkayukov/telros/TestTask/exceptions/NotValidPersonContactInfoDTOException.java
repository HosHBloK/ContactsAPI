package com.igorkayukov.telros.TestTask.exceptions;

public class NotValidPersonContactInfoDTOException extends RuntimeException {

	private static final long serialVersionUID = 5203320720124498442L;

	public NotValidPersonContactInfoDTOException(String msg) {
		super(msg);
	}
}