package com.rivera.springboot.backend.apirest.challenge.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationExceptions(ArgumentNotValidException ex) {
		Map<String, Object> response = new HashMap<>();

		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
				.collect(Collectors.toList());

		response.put("message", "Error de validación de campos");
		response.put("errors", errors);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateRecordOrBadRequestException.class)
	public ResponseEntity<Object> handleDuplicateRecordException(DuplicateRecordOrBadRequestException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Error al realizar operación en la base de datos.");
		response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex, WebRequest request) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Error en el servidor");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
