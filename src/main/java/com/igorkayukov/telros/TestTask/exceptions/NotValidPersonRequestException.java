package com.igorkayukov.telros.TestTask.exceptions;

public class NotValidPersonRequestException extends RuntimeException {

	private static final long serialVersionUID = 5203320720124498442L;

	public NotValidPersonRequestException(String msg) {
		super(msg);
	}
}