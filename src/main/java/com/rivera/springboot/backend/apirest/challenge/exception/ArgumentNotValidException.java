package com.rivera.springboot.backend.apirest.challenge.exception;

import org.springframework.validation.BindingResult;

public class ArgumentNotValidException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private BindingResult bindingResult;

	public ArgumentNotValidException(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}
}
