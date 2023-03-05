package ru.yandex.practicum.filmorate.type;

public final class UserIdType extends ValueType<Long> {
    public UserIdType(Long value) {
        super(value);
    }

    public static UserIdType of(Long _value) {
        return new UserIdType(_value);
    }
}
