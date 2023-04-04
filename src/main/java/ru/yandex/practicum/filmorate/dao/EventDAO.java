package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.type.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventDAO {
    private static final String ID_FIELD = "event_id";
    private static final String TIMESTAMP_FIELD = "time_stamp";
    private static final String EVENT_TYPE_FIELD = "event_type";
    private static final String OPERATION_FIELD = "operation";
    private static final String USER_FIELD = "user_id";
    private static final String ENTITY_FIELD = "entity_id";
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    public void addEvent(UserIdType userId, ValueType<Long> entityId, EventType eventType, OperationType operationType) {
        String sql = String.format("insert into Events ( %1$s,  %2$s,  %3$s,  %4$s) " +
                        "values ( :%1$s,  :%2$s,  :%3$s,  :%4$s)",
                EVENT_TYPE_FIELD, OPERATION_FIELD, USER_FIELD, ENTITY_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(EVENT_TYPE_FIELD, eventType.toString())
                .addValue(OPERATION_FIELD, operationType.toString())
                .addValue(USER_FIELD, userId.getValue())
                .addValue(ENTITY_FIELD, entityId.getValue());
        int rowCount = jdbcNamedTemplate.update(sql, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(UserDAO.idNotFoundMsg(userId), this.getClass(), log);
        }

        log.debug("Выполнено {}.addEvent({}, {}, {}, {})", this.getClass().getName(), userId, entityId, eventType, operationType);
    }

    public List<Event> getFeedForUser(UserIdType userId) {
        String sql = String.format("Select * from Events where %1$s = :%1$s order by %2$s ", USER_FIELD, ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(USER_FIELD, userId.getValue());

        List<Event> result = jdbcNamedTemplate.query(sql, sqlParams, this::mapRowToEvent);

        log.info("Выполнено {}.getFeedForUser({})", this.getClass(), userId);

        return result;
    }

    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .timestamp(rs.getTimestamp(TIMESTAMP_FIELD).toInstant().toEpochMilli())
                .userId(UserIdType.of(rs.getLong(USER_FIELD)))
                .eventType(rs.getString(EVENT_TYPE_FIELD))
                .operation(rs.getString(OPERATION_FIELD))
                .eventId(EventIdType.of(rs.getLong(ID_FIELD)))
                .entityId(rs.getLong(ENTITY_FIELD))
                .build();
    }
}