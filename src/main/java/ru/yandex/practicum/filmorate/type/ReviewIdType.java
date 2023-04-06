package ru.yandex.practicum.filmorate.type;

import java.io.Serializable;

public final class ReviewIdType extends ValueType<Long> implements Serializable {
    public ReviewIdType(Long value) {
        super(value);
    }

    public static ReviewIdType of(Long value) {
        return new ReviewIdType(value);
    }
}
