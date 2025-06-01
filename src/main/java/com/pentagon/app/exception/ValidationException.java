package com.pentagon.app.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> errors;
	private HttpStatus httpStatus;

	public ValidationException(String message, List<String> errors, HttpStatus httpStatus) {
		super(message);
		this.errors = errors;
		this.httpStatus = httpStatus;
		this.httpStatus = httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR;

	}

	public List<String> getErrors() {
		return errors;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
