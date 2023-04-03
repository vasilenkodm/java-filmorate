package ru.yandex.practicum.filmorate.storage.event;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.type.EventType;
import ru.yandex.practicum.filmorate.type.OperationType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

public interface EventStorage {

    void addEvent(Long userId, Long entityId, EventType eventType, OperationType operationType);

    List<Event> getFeedForUser(UserIdType userId);
}
