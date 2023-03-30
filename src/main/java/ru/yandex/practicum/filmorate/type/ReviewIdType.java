package ru.yandex.practicum.filmorate.type;

import java.io.Serializable;

public final class ReviewIdType extends ValueType<Integer> implements Serializable {
    public ReviewIdType(Integer value) {
        super(value);
    }

    public static ReviewIdType of(Integer value) {
        return new ReviewIdType(value);
    }
}
