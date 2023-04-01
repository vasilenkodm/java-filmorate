package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.type.EventType;
import ru.yandex.practicum.filmorate.type.OperationType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventDAO {
    private static final String ID_EVENT_FIELD = "event_id";
    private static final String TIMESTAMP_FIELD = "time_stamp";
    private static final String EVENT_TYPE_FIELD = "event_type";
    private static final String OPERATION_FIELD = "operation";
    private static final String ID_FIELD = "user_id";
    private static final String ENTITY_ID_FIELD = "entity_id";
    private final JdbcTemplate jdbcTemplate;

    public void addEvent(Long userId, Long entityId, EventType eventType, OperationType operationType) {
        long timestamp = Instant.now().toEpochMilli();
        String sql = "insert into Events (time_stamp, event_type, operation, user_id, entity_id) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, timestamp, eventType.toString(), operationType.toString(), userId, entityId);
    }

    public List<Event> getFeedForUser(UserIdType userId) {
        String sql = "Select * from Events where user_id = ?";
        List<Event> userFeed = jdbcTemplate.query(sql, this::mapRowToEvent, userId.getValue());

        if (userFeed.isEmpty()) {
            throw new KeyNotFoundException("Пользователь не найден.", this.getClass(), log);
        }
        log.info("Выполнено {}.getFeedForUser({})", this.getClass(), userId.getValue());
        return userFeed;
    }

    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getLong(ID_EVENT_FIELD))
                .timestamp(rs.getLong(TIMESTAMP_FIELD))
                .eventType(rs.getString(EVENT_TYPE_FIELD))
                .operation(rs.getString(OPERATION_FIELD))
                .userId(UserIdType.of(rs.getLong(ID_FIELD)))
                .entityId(rs.getLong(ENTITY_ID_FIELD))
                .build();
    }
}