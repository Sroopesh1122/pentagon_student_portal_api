package com.pentagon.app.exception;

import org.springframework.http.HttpStatus;

public class ManagerException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		private HttpStatus httpStatus;

		public ManagerException(String message , HttpStatus httpStatus) {
			super(message);
			this.httpStatus = httpStatus;
			this.httpStatus = httpStatus!=null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR;	
		}

		public HttpStatus getHttpStatus() {
			return httpStatus;
		}
	}

