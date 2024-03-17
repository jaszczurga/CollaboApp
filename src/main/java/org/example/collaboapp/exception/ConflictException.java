package org.example.collaboapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

    public ConflictException(String resourceName) {
        super(String.format("Conflict occured with resource: "+resourceName));
    }

}