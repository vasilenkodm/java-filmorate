package ru.yandex.practicum.filmorate.type;

public class FilmIdType extends ValueType<Long> {
    public FilmIdType(final Long value) {
        super(value);
    }

    public static FilmIdType of(final Long _value) {
        return new FilmIdType(_value);
    }
}
