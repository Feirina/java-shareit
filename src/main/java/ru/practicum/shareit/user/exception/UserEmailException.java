package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserEmailException extends RuntimeException {
    public UserEmailException(String message) {
        super(message);
    }
}
