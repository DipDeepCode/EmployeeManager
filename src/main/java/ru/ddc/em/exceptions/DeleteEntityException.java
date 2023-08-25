package ru.ddc.em.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeleteEntityException extends RuntimeException {
    public DeleteEntityException(String message) {
        super(message);
    }
}
