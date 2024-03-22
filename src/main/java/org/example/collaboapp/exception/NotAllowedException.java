package org.example.collaboapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
public class NotAllowedException extends RuntimeException {

    public NotAllowedException(String resourceName) {
        super(String.format(" Method not allowed -> "+resourceName));
    }

}
