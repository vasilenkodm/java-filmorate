package ru.yandex.practicum.filmorate.type;

public class UserIdType extends ValueType<Long> {
    public UserIdType(Long value) {
        super(value);
    }

    public static UserIdType of(final Long _value) {
        return new UserIdType(_value);
    }
}
