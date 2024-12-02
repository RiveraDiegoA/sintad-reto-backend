package com.rivera.springboot.backend.apirest.challenge.exception;

public class DuplicateRecordOrBadRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DuplicateRecordOrBadRequestException(String message) {
		super(message);
	}
}
