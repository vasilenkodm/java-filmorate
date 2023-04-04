package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.time.Instant;


@Getter
@Setter
@Builder
public class Event {
    private long eventId;
    private Instant timestamp;
    private String eventType;
    private String operation;
    private UserIdType userId;
    private long entityId;
}