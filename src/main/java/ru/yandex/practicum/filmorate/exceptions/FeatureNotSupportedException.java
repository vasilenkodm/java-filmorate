package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.slf4j.Logger;

public class FeatureNotSupportedException extends RuntimeException {
    @Getter
    private final Class<?> keyOwnerClass;

    public FeatureNotSupportedException(String message, Class<?> keyOwnerClass, Logger log) {
        super(message);
        this.keyOwnerClass = keyOwnerClass;
        log.warn(message);
    }

    public FeatureNotSupportedException(Class<?> keyOwnerClass, Logger log) {
        this("Не реализовано", keyOwnerClass, log);
    }

}
