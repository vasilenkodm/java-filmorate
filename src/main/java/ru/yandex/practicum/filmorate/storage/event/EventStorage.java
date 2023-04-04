package ru.yandex.practicum.filmorate.storage.event;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.type.EventType;
import ru.yandex.practicum.filmorate.type.OperationType;
import ru.yandex.practicum.filmorate.type.UserIdType;
import ru.yandex.practicum.filmorate.type.ValueType;

import java.util.List;

public interface EventStorage {

    void addEvent(UserIdType userId, ValueType<Long> entityId, EventType eventType, OperationType operationType);

    List<Event> getFeedForUser(UserIdType userId);
}
