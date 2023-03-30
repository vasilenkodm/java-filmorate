package ru.yandex.practicum.filmorate.storage.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventDAO;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.type.EventType;
import ru.yandex.practicum.filmorate.type.OperationType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class EventDbStorage implements EventStorage {

    private final EventDAO dao;

    @Override
    public void addEvent(Long userId, Long entityId, EventType eventType, OperationType operationType) {
        dao.addEvent(userId, entityId, eventType, operationType);
    }

    @Override
    public List<Event> getFeedForUser(UserIdType userId) {
        return dao.getFeedForUser(userId);
    }
}