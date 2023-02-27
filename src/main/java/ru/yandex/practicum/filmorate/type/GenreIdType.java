package ru.yandex.practicum.filmorate.type;

public class GenreIdType extends SomeType<Integer> {
    public GenreIdType(Integer value) { super(value); }
    public static GenreIdType of(final Integer _value) { return new GenreIdType(_value); }
}
