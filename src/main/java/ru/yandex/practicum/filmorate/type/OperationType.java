package ru.yandex.practicum.filmorate.type;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OperationType {
    ADD,
    REMOVE,
    UPDATE
}
