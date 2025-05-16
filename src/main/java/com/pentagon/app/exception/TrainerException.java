package com.pentagon.app.exception;



import org.springframework.http.HttpStatus;

public class TrainerException extends RuntimeException {
    private HttpStatus httpStatus;
    private static final long serialVersionUID = 1L;

    public TrainerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}