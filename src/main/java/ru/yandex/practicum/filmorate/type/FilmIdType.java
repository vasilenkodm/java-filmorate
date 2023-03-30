package ru.yandex.practicum.filmorate.type;

import java.io.Serializable;

public final class FilmIdType extends ValueType<Long> implements Serializable {
    public FilmIdType(Long value) {
        super(value);
    }

    public static FilmIdType of(Long value) {
        return new FilmIdType(value);
    }
}
