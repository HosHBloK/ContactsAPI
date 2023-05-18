package com.igorkayukov.telros.TestTask.exceptions;

public class NotValidPersonDetailedInfoDTOException extends RuntimeException {

	private static final long serialVersionUID = 4771699398070276219L;

	public NotValidPersonDetailedInfoDTOException(String msg) {
		super(msg);
	}
}