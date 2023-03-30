package ru.yandex.practicum.filmorate.type;

public final class UserIdType extends ValueType<Long> {
    public UserIdType(Long value) {
        super(value);
    }

    public static UserIdType of(Long value) {
        return new UserIdType(value);
    }
}
