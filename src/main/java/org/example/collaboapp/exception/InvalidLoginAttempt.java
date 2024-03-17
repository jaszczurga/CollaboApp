package org.example.collaboapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidLoginAttempt extends RuntimeException {
    public InvalidLoginAttempt(String message) {
        super(message);
    }
}