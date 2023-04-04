package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.type.EventIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;


@Getter
@Setter
@Builder
public class Event {
    private EventIdType eventId;
    private long timestamp;
    private String eventType;
    private String operation;
    private UserIdType userId;
    private Long entityId;
}