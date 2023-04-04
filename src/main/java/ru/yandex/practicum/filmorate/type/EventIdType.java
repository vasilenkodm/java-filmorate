package ru.yandex.practicum.filmorate.type;

public final class EventIdType extends ValueType<Long> {
    public EventIdType(Long value) {
        super(value);
    }

    public static EventIdType of(Long value) {
        return new EventIdType(value);
    }
}
