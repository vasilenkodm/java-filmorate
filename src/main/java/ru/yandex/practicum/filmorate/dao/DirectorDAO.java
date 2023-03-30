package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.type.DirectorIdType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DirectorDAO implements ItemDAO<DirectorIdType, Director> {

    public static final String ID_FIELD = "Director_id";
    public static final String NAME_FIELD = "Director_name";
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    public static String idNotFoundMsg(DirectorIdType _id) {
        return String.format("Не найден режиссер с кодом %s!", _id);
    }

    public boolean notExists(DirectorIdType _id) {
        String sqlStatement = String.format("select count(*) from Director where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        Integer count = Objects.requireNonNull(jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, Integer.class));
        log.info("Выполнено {}.exists({})", this.getClass().getName(), _id);
        return count == 0;
    }

    public Director read(final DirectorIdType _id) {
        String sqlStatement = String.format("select * from Director where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        Director result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeDirector(rs));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.read({})", this.getClass().getName(), _id);

        return result;
    }

    public List<Director> selectAll() {
        final String sqlStatement = String.format("select * from Director order by %1$s", ID_FIELD);
        List<Director> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeDirector(rs));

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public Director create(Director _source) {
        final String sqlStatement = String.format("insert into Director (%1$s) values ( :%1$s )", NAME_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _source.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});
        DirectorIdType newId = DirectorIdType.of(Objects.requireNonNull(keyHolder.getKey()).intValue());
        Director result = (Director) _source.clone(); //Развязываем образец и результат
        result.setId(newId);
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), _source, newId);
        return result;
    }

    public void update(Director _Director) {
        final String sqlStatement = String.format("update Director set %1$s = :%1$s where %2$s = :%2$s", NAME_FIELD, ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _Director.getName())
                .addValue(ID_FIELD, _Director.getId().getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(_Director.getId()), this.getClass(), log);
        }

        log.info("Выполнено {}.update({})", this.getClass().getName(), _Director);
    }

    public void delete(DirectorIdType _id) {
        final String sqlStatement = String.format("delete from Director where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.delete({})", this.getClass().getName(), _id);
    }

    public static Director makeDirector(ResultSet _rs) throws SQLException {
        log.debug("Вызов {}.makeDirector({})", DirectorDAO.class.getName(), _rs);
        return Director.builder()
                .id(DirectorIdType.of(_rs.getInt(ID_FIELD)))
                .name(_rs.getString(NAME_FIELD))
                .build();
    }

}
