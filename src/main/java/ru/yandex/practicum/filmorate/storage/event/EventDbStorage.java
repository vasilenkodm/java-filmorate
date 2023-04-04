package ru.yandex.practicum.filmorate.storage.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventDAO;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.type.EventType;
import ru.yandex.practicum.filmorate.type.OperationType;
import ru.yandex.practicum.filmorate.type.UserIdType;
import ru.yandex.practicum.filmorate.type.ValueType;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class EventDbStorage implements EventStorage {

    private final EventDAO dao;
    private final UserDAO userDAO;

    @Override
    public void addEvent(UserIdType userId, ValueType<Long> entityId, EventType eventType, OperationType operationType) {
        dao.addEvent(userId, entityId, eventType, operationType);
    }

    @Override
    public List<Event> getFeedForUser(UserIdType userId) {
        if (userDAO.notExists(userId)) {
            throw new KeyNotFoundException(UserDAO.idNotFoundMsg(userId), this.getClass(), log);
        }
        return dao.getFeedForUser(userId);
    }
}