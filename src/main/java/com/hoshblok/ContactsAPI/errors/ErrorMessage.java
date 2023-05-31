package com.hoshblok.ContactsAPI.errors;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorMessage {

	public static String getErrorMessage(BindingResult bindingResult) {

		StringBuilder errorMessage = new StringBuilder();
		List<FieldError> errorList = bindingResult.getFieldErrors();
		for (FieldError error : errorList) {
			errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ");
		}

		return errorMessage.toString();
	}
}