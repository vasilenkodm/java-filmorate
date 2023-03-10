package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UnexpectedErrorException extends RuntimeException {
    @Getter
    private final Class<?> keyOwnerClass;

    public UnexpectedErrorException(String message, Class<?> keyOwnerClass, Logger log) {
        super(message);
        this.keyOwnerClass = keyOwnerClass;
        log.warn(message);
    }
}
