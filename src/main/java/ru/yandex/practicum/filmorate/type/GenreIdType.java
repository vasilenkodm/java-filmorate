package ru.yandex.practicum.filmorate.type;

public final class GenreIdType extends ValueType<Integer> {
    public GenreIdType(Integer value) {
        super(value);
    }

    public static GenreIdType of(Integer value) {
        return new GenreIdType(value);
    }
}
