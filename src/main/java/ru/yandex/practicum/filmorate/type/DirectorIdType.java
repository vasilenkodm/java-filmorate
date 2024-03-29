package ru.yandex.practicum.filmorate.type;

import java.io.Serializable;

public final class DirectorIdType extends ValueType<Integer> implements Serializable {
    public DirectorIdType(Integer value) {
        super(value);
    }

    public static DirectorIdType of(Integer value) {
        return new DirectorIdType(value);
    }
}
