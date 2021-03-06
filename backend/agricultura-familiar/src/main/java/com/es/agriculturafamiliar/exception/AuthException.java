package com.es.agriculturafamiliar.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthException extends RuntimeException {
    private static final long serialVersionUID = -8162365112708335074L;
	private String title;
    private String message;
    private HttpStatus httpStatus;
    
    public AuthException(String message, String title, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.title = title;
        this.httpStatus = httpStatus;
    }

}
